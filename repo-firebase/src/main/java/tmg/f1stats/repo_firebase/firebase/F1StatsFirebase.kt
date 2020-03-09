package tmg.f1stats.repo_firebase.firebase

import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import tmg.f1stats.repo.errors.PermissionError

val firebaseApp: FirebaseApp = FirebaseApp.getInstance()

private fun document(documentPath: String): DocumentReference = FirebaseFirestore
    .getInstance(firebaseApp)
    .document(documentPath)

private fun collection(collectionPath: String): CollectionReference = FirebaseFirestore
    .getInstance(firebaseApp)
    .collection(collectionPath)

fun <T, E: Any> addDocument(collectionPath: String, model: T, toModel: (model: T) -> E) {
    collection(collectionPath)
        .add(toModel(model))
}

inline fun <T, reified E> getDocuments(zClass: Class<E>, collectionPath: String, convertTo: (firebaseModel: E) -> T): Observable<List<T>> {
    return Observable.create { emitter ->


        val snapshotListener: EventListener<QuerySnapshot> = EventListener<QuerySnapshot> { snapshot, exception ->
            if (exception != null) {
                when (exception.code) {
                    FirebaseFirestoreException.Code.OK -> TODO()
                    FirebaseFirestoreException.Code.CANCELLED -> TODO()
                    FirebaseFirestoreException.Code.UNKNOWN -> TODO()
                    FirebaseFirestoreException.Code.INVALID_ARGUMENT -> TODO()
                    FirebaseFirestoreException.Code.DEADLINE_EXCEEDED -> TODO()
                    FirebaseFirestoreException.Code.NOT_FOUND -> TODO()
                    FirebaseFirestoreException.Code.ALREADY_EXISTS -> TODO()
                    FirebaseFirestoreException.Code.PERMISSION_DENIED -> emitter.onError(PermissionError(collectionPath))
                    FirebaseFirestoreException.Code.RESOURCE_EXHAUSTED -> TODO()
                    FirebaseFirestoreException.Code.FAILED_PRECONDITION -> TODO()
                    FirebaseFirestoreException.Code.ABORTED -> TODO()
                    FirebaseFirestoreException.Code.OUT_OF_RANGE -> TODO()
                    FirebaseFirestoreException.Code.UNIMPLEMENTED -> TODO()
                    FirebaseFirestoreException.Code.INTERNAL -> TODO()
                    FirebaseFirestoreException.Code.UNAVAILABLE -> TODO()
                    FirebaseFirestoreException.Code.DATA_LOSS -> TODO()
                    FirebaseFirestoreException.Code.UNAUTHENTICATED -> TODO()
                }
            }
        }

        collection(collectionPath)
            .addSnapshotListener(snapshotListener)

        emitter.setDisposable(Disposable.fromAction {

        })
    }
}