package tmg.flashback.firebase

import com.google.firebase.firestore.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import tmg.flashback.firebase.crash.FirebaseCrashManager

open class FirebaseRepo(
    val crashManager: FirebaseCrashManager
) {

    //#region References

    protected fun document(documentPath: String): DocumentReference = FirebaseFirestore
        .getInstance(firebaseApp)
        .document(documentPath)

    protected fun collection(collectionPath: String): CollectionReference = FirebaseFirestore
        .getInstance(firebaseApp)
        .collection(collectionPath)

    //#endregion

    // TODO: May be able to remove this?
    inline fun <reified E> CollectionReference.getDocuments(
        default: List<E> = emptyList(),
        crossinline query: (ref: CollectionReference) -> Query = { it }
    ): Flow<List<E>> = callbackFlow {
        val subscription = query(this@getDocuments).addSnapshotListener { snapshot, exception ->
            when {
                exception != null -> {
                    handleError(exception, "Collection $path")
                    offer(emptyList<E>())
                }
                snapshot != null -> {
                    try {
                        val list: List<E?> = snapshot.documents.map { it.toObject(E::class.java) }
                        @Suppress("SimplifiableCall")
                        offer(list.filter { it != null }.map { it as E })
                    } catch (e: RuntimeException) {
                        if (BuildConfig.DEBUG) {
                            throw e
                        } else {
                            handleError(e, "getDocuments under $path failed to parse")
                            offer(default)
                        }
                    }
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
                    handleError(exception, "Collection $path")
                    offer(null)
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
                    } catch (e: RuntimeException) {
                        if (BuildConfig.DEBUG) {
                            throw e
                        } else {
                            handleError(e, "getDoc under $path failed to parse")
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
            val context = "Error thrown that isn't a firebase error ${exception::class.simpleName} - Path $path"
            val wrappedException = Exception(context, exception)
            crashManager.logException(wrappedException, context)
        }
    }

    private fun handleFirebaseError(exception: FirebaseFirestoreException, path: String) {
        exception.printStackTrace()
        when (exception.code) {
            FirebaseFirestoreException.Code.OK -> { }
            FirebaseFirestoreException.Code.CANCELLED -> { }
            FirebaseFirestoreException.Code.NOT_FOUND -> {
                crashManager.logException(exception, "Accessing $path resulted in not found")
            } // this.onError(DoesntExistError(debugData))
            FirebaseFirestoreException.Code.ALREADY_EXISTS -> {
                crashManager.logException(exception, "Already exists accessing $path")
            } // this.onError(RecordAlreadyExistsError(debugData))
            FirebaseFirestoreException.Code.PERMISSION_DENIED -> {
                crashManager.logException(exception, "Permission denied while accessing $path")
            } // this.onError(PermissionError(debugData))
            FirebaseFirestoreException.Code.UNAUTHENTICATED -> {
                crashManager.logException(exception, "Unauthenticated while accessing $path")
            } // this.onError(UnauthenticatedError(debugData))
            else -> {
                crashManager.logException(exception, "Unsupported error thrown by Firebase $path")
            } // this.onError(Error("Error occurred ${exception.code}"))
        }
    }
}