package tmg.f1stats.repo_firebase.firebase

import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.disposables.Disposable
import tmg.f1stats.repo.errors.DoesntExistError
import tmg.f1stats.repo.errors.PermissionError
import tmg.f1stats.repo.errors.RecordAlreadyExistsError
import tmg.f1stats.repo.errors.UnauthenticatedError

val firebaseApp: FirebaseApp = FirebaseApp.getInstance()

fun document(documentPath: String): DocumentReference = FirebaseFirestore
        .getInstance(firebaseApp)
        .document(documentPath)

fun collection(collectionPath: String): CollectionReference = FirebaseFirestore
        .getInstance(firebaseApp)
        .collection(collectionPath)

fun <T, E : Any> addDocument(collectionPath: String, model: T, toModel: (model: T) -> E) {
    collection(collectionPath)
            .add(toModel(model))
}

inline fun <T, reified E> getDocument(zClass: Class<E>, documentPath: String, crossinline convertTo: (firebaseModel: E, id: String) -> T): Observable<T> {
    return Observable.create { emitter ->
        val snapshotListener: EventListener<DocumentSnapshot> = EventListener<DocumentSnapshot> { snapshot, exception ->
            if (exception != null) {
                emitter.handleFirebaseError(exception, documentPath)
            }
            else {
                val firebaseObj = snapshot?.toObject(zClass)!!
                emitter.onNext(convertTo(firebaseObj, snapshot.id))
            }
        }

        val registration = document(documentPath)
                .addSnapshotListener(snapshotListener)

        emitter.setDisposable(Disposable.fromAction {
            registration.remove()
        })
    }
}

inline fun <T, reified E> getDocumentMap(zClass: Class<E>, documentPath: String, crossinline convertTo: (firebaseModel: E) -> T): Observable<List<T>> {
    return Observable.create { emitter ->
        val snapshotListener: EventListener<DocumentSnapshot> = EventListener<DocumentSnapshot> { snapshot, exception ->
            if (exception != null) {
                emitter.handleFirebaseError(exception, documentPath)
            }
            else {
                val keys = snapshot?.data?.keys
                emitter.onNext(keys
                        ?.map { convertTo(snapshot.get(it, zClass)!!) }
                        ?: emptyList()
                )
            }
        }

        val registration = document(documentPath)
                .addSnapshotListener(snapshotListener)

        emitter.setDisposable(Disposable.fromAction {
            registration.remove()
        })
    }
}

inline fun <T, reified E> getDocuments(firebaseClass: Class<E>, collectionPath: String, crossinline convertTo: (firebaseModel: E, id: String) -> T): Observable<List<T>> {
    return Observable.create { emitter ->
        val snapshotListener: EventListener<QuerySnapshot> = EventListener<QuerySnapshot> { snapshot, exception ->
            if (exception != null) {
                emitter.handleFirebaseError(exception, collectionPath)
            } else {
                emitter.onNext(snapshot?.documents
                        ?.filter { it.data != null }
                        ?.map {
                            val firebaseObj = it.toObject(firebaseClass)!!
                            return@map convertTo(firebaseObj, it.id)
                        }
                        ?: listOf()
                )
            }
        }

        val registration = collection(collectionPath)
                .addSnapshotListener(snapshotListener)

        emitter.setDisposable(Disposable.fromAction {
            registration.remove()
        })
    }
}

fun <T> ObservableEmitter<T>.handleFirebaseError(exception: FirebaseFirestoreException, debugData: String) {
    when (exception.code) {
        FirebaseFirestoreException.Code.OK -> {}
        FirebaseFirestoreException.Code.CANCELLED -> {}
        FirebaseFirestoreException.Code.NOT_FOUND -> this.onError(DoesntExistError(debugData))
        FirebaseFirestoreException.Code.ALREADY_EXISTS -> this.onError(RecordAlreadyExistsError(debugData))
        FirebaseFirestoreException.Code.PERMISSION_DENIED -> this.onError(PermissionError(debugData))
        FirebaseFirestoreException.Code.UNAUTHENTICATED -> this.onError(UnauthenticatedError(debugData))
        else -> this.onError(Error("Error occurred ${exception.code}"))
    }
}