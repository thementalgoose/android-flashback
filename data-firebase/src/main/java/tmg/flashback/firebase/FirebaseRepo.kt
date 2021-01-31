package tmg.flashback.firebase

import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map

open class FirebaseRepo(
    val crashManager: FirestoreCrashManager
) {

    private val firebaseApp: FirebaseApp = FirebaseApp.getInstance()

    //#region References

    /**
     * Firestore document instance retreiver
     * @param documentPath Path to document in firestore
     */
    protected fun document(documentPath: String): DocumentReference = FirebaseFirestore
        .getInstance(firebaseApp)
        .document(documentPath)

    /**
     * Firestore collection instance retreiver
     * @param collectionPath Path to collection in firestore
     */
    @Deprecated("This should not be used due to exponential cost concerns. Consider using document() to access dirctly",
        ReplaceWith("document(documentPath)")
    )
    protected fun collection(collectionPath: String): CollectionReference = FirebaseFirestore
        .getInstance(firebaseApp)
        .collection(collectionPath)

    //#endregion

    /**
     * Get a document from firestore and convert into a domain level model
     *
     * @param converter Method which takes firestore model (denoted by prefix F (ie. FModel)) into a
     *   domain model
     */
    inline fun <reified E,T> DocumentReference.getDoc(crossinline converter: (E) -> T): Flow<T?> = callbackFlow {
        val subscription = addSnapshotListener { snapshot, exception ->
            when {
                exception != null -> {
                    if (BuildConfig.DEBUG) {
                        Log.e("Flashback", "Snapshot exception ${exception.message}")
                        throw exception
                    } else {
                        handleError(exception, path)
                        offer(null)
                    }
                }
                snapshot != null -> {
                    try {
                        val item: E? = snapshot.toObject(E::class.java)
                        if (item != null) {
                            val model = converter(item)
                            offer(model)
                        }
                        else {
                            offer(null)
                        }
                    } catch (e: java.lang.Exception) {
                        if (BuildConfig.DEBUG) {
                            Log.e("Flashback", "Conversion exception ${e.message}")
                            throw e
                        } else {
                            handleError(e, path)
                            offer(null)
                        }
                    }
                }
                else -> {
                    offer(null)
                }
            }
        }
        awaitClose {
            subscription.remove()
        }
    }

    fun <E, T> Flow<List<E>>.convertModels(convert: (model: E) -> T): Flow<List<T>> {
        return this.map { list -> list.map { convert(it) } }
    }

    fun <E> Flow<E?>.defaultIfNull(to: E): Flow<E> {
        return this.map { it ?: to }
    }

    fun <E> Flow<List<E>?>.emptyIfNull(): Flow<List<E>> {
        return this.map { it ?: emptyList() }
    }

    fun handleError(exception: Exception, path: String) {
        if (exception is FirebaseFirestoreException) {
            handleFirebaseError(exception, path)
        } else {
            val wrappedException = FlashbackParseException("Model at $path failed to parse", exception)
            crashManager.logException(wrappedException, "Exception thrown whilst parsing model on $path - ${exception.message}")
        }
    }

    private fun handleFirebaseError(exception: FirebaseFirestoreException, path: String) {
        exception.printStackTrace()
        when (exception.code) {
            FirebaseFirestoreException.Code.OK -> { }
            FirebaseFirestoreException.Code.CANCELLED -> { }
            FirebaseFirestoreException.Code.NOT_FOUND -> {
                crashManager.logException(exception, "Accessing $path resulted in not found")
            }
            FirebaseFirestoreException.Code.ALREADY_EXISTS -> {
                crashManager.logException(exception, "Already exists accessing $path")
            }
            FirebaseFirestoreException.Code.PERMISSION_DENIED -> {
                crashManager.logException(exception, "Permission denied while accessing $path")
            }
            FirebaseFirestoreException.Code.UNAUTHENTICATED -> {
                crashManager.logException(exception, "Unauthenticated while accessing $path")
            }
            else -> {
                crashManager.logException(exception, "Unsupported error thrown by Firebase $path")
            }
        }
    }
}