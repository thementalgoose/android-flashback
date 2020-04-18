package tmg.flashback.repo_firebase.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.tasks.await
import tmg.flashback.repo.db.CrashReporter

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



suspend inline fun <reified E, T> getDocument(documentPath: String, crashReporter: CrashReporter? = null, default: T, crossinline convertTo: (firebaseModel: E, id: String) -> T): Flow<T> = callbackFlow {
    val subscription = document(documentPath).addSnapshotListener { documentSnapshot, exception ->
        when {
            exception != null -> {
                handleError(exception, crashReporter, "Document $documentPath")

            }
            documentSnapshot != null -> {
                val result = convertTo(documentSnapshot.toObject(E::class.java)!!, documentSnapshot.id)
                offer(result)
            }
            else -> {
                offer(default)
            }
        }
    }

    awaitClose {
        subscription.remove()
    }
}

//suspend inline fun <T, reified E> getDocument(zClass: Class<E>, documentPath: String, crashReporter: CrashReporter? = null, convertTo: (firebaseModel: E, id: String) -> T): T? {
//    return try {
//        val snapshot = document(documentPath).get().await()
//        println(snapshot)
//        convertTo(snapshot?.toObject(zClass)!!, snapshot.id)
//    }
//    catch (e: NullPointerException) {
//        null
//    }
//    catch (e: FirebaseFirestoreException) {
//        handleError(e, crashReporter, "Document $documentPath")
//        null
//    }
//}

suspend inline fun <T, reified E> getDocumentMap(zClass: Class<E>, documentPath: String, crashReporter: CrashReporter? = null, convertTo: (firebaseModel: E) -> T): List<T> {
    return try {
        val snapshot = document(documentPath).get().await()
        val keys = snapshot?.data?.keys
        keys?.map { convertTo(snapshot.get(it, zClass)!!) } ?: emptyList()
    }
    catch (e: FirebaseFirestoreException) {
        handleError(e, crashReporter, "Document $documentPath")
        emptyList()
    }
}

suspend inline fun <T, reified E> getDocuments(firebaseClass: Class<E>, collectionPath: String, crashReporter: CrashReporter? = null,convertTo: (firebaseModel: E, id: String) -> T): List<T> {
    return try {
        val snapshot = collection(collectionPath).get().await()
        snapshot?.documents
            ?.filter { it.data != null }
            ?.map {
                val firebaseObj = it.toObject(firebaseClass)!!
                return@map convertTo(firebaseObj, it.id)
            }
            ?: listOf()
    }
    catch (e: FirebaseFirestoreException) {
        handleError(e, crashReporter, "Collection $collectionPath")
        emptyList()
    }
}

fun handleError(exception: FirebaseFirestoreException, crashReporter: CrashReporter? = null, path: String) {
    exception.printStackTrace()
    when (exception.code) {
        FirebaseFirestoreException.Code.OK -> {}
        FirebaseFirestoreException.Code.CANCELLED -> {}
        FirebaseFirestoreException.Code.NOT_FOUND -> {
            crashReporter?.logError(exception, "Accessing $path resulted in not found")
        } // this.onError(DoesntExistError(debugData))
        FirebaseFirestoreException.Code.ALREADY_EXISTS -> {
            crashReporter?.logError(exception, "Already exists accessing $path")
        } // this.onError(RecordAlreadyExistsError(debugData))
        FirebaseFirestoreException.Code.PERMISSION_DENIED -> {
            crashReporter?.logError(exception, "Permission denied while accessing $path")
        } // this.onError(PermissionError(debugData))
        FirebaseFirestoreException.Code.UNAUTHENTICATED -> {
            crashReporter?.logError(exception, "Unauthenticated while accessing $path")
        } // this.onError(UnauthenticatedError(debugData))
        else -> {
            crashReporter?.logError(exception, "Unsupported error thrown by Firebase $path")
        } // this.onError(Error("Error occurred ${exception.code}"))
    }
}
