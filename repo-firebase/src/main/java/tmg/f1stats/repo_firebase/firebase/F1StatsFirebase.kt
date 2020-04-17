package tmg.f1stats.repo_firebase.firebase

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resumeWithException

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

suspend inline fun <T, reified E> getDocument(zClass: Class<E>, documentPath: String, crossinline convertTo: (firebaseModel: E, id: String) -> T): T? {
    return try {
        val snapshot = document(documentPath).get().await()
        convertTo(snapshot?.toObject(zClass)!!, snapshot.id)
    }
    catch (e: FirebaseFirestoreException) {
        handleError(e)
        null
    }
}

suspend inline fun <T, reified E> getDocumentMap(zClass: Class<E>, documentPath: String, crossinline convertTo: (firebaseModel: E) -> T): List<T> {
    return try {
        val snapshot = document(documentPath).get().await()
        val keys = snapshot?.data?.keys
        keys?.map { convertTo(snapshot.get(it, zClass)!!) } ?: emptyList()
    }
    catch (e: FirebaseFirestoreException) {
        handleError(e)
        emptyList()
    }
}

suspend inline fun <T, reified E> getDocuments(firebaseClass: Class<E>, collectionPath: String, crossinline convertTo: (firebaseModel: E, id: String) -> T): List<T> {
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
        handleError(e)
        emptyList()
    }
}

fun handleError(exception: FirebaseFirestoreException) {
    exception.printStackTrace()
    when (exception.code) {
        FirebaseFirestoreException.Code.OK -> {}
        FirebaseFirestoreException.Code.CANCELLED -> {}
        FirebaseFirestoreException.Code.NOT_FOUND -> {} // this.onError(DoesntExistError(debugData))
        FirebaseFirestoreException.Code.ALREADY_EXISTS -> {} // this.onError(RecordAlreadyExistsError(debugData))
        FirebaseFirestoreException.Code.PERMISSION_DENIED -> {} // this.onError(PermissionError(debugData))
        FirebaseFirestoreException.Code.UNAUTHENTICATED -> {} // this.onError(UnauthenticatedError(debugData))
        else -> {} // this.onError(Error("Error occurred ${exception.code}"))
    }
}