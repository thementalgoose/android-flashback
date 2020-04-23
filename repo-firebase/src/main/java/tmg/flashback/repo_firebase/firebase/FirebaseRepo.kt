package tmg.flashback.repo_firebase.firebase

import com.google.firebase.firestore.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import tmg.flashback.repo.db.CrashReporter

open class FirebaseRepo(
    val crashReporter: CrashReporter
) {

    //#region References

    protected fun document(documentPath: String): DocumentReference = FirebaseFirestore
        .getInstance(firebaseApp)
        .document(documentPath)

    protected fun collection(collectionPath: String): CollectionReference = FirebaseFirestore
        .getInstance(firebaseApp)
        .collection(collectionPath)

    //#endregion

    inline fun <reified E> CollectionReference.getDocuments(
        default: List<E> = emptyList(),
        crossinline query: (ref: CollectionReference) -> Query
    ): Flow<List<E>> = callbackFlow {
        val subscription = query(this@getDocuments).addSnapshotListener { snapshot, exception ->
            when {
                exception != null -> {
                    handleError(exception, "Collection $path")
                    offer(emptyList())
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
                            offer(emptyList())
                        }
                    }
                }
                else -> {
                    offer(emptyList())
                }
            }
        }
        awaitClose {
            subscription.remove()
        }
    }

    inline fun <reified E> DocumentReference.getDoc(): Flow<E?> = callbackFlow {
        val subscription = addSnapshotListener { snapshot, exception ->
            when {
                exception != null -> {
                    handleError(exception, "Collection $path")
                    offer(null)
                }
                snapshot != null -> {
                    try {
                        val item: E? = snapshot.toObject(E::class.java)
                        offer(item)
                    } catch (e: RuntimeException) {
                        if (BuildConfig.DEBUG) {
                            throw e
                        } else {
                            handleError(e, "getDocuments under $path failed to parse")
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

    fun <E, T> Flow<E>.convertModel(convert: (model: E) -> T): Flow<T> {
        return this.map { convert(it) }
    }

    fun handleError(exception: Exception, path: String) {
        if (exception is FirebaseFirestoreException) {
            handleFirebaseError(exception, path)
        } else {
            crashReporter.logError(
                exception,
                "Error thrown that isn't a firebase error ${exception::class.simpleName} - Path $path"
            )
        }
    }

    private fun handleFirebaseError(exception: FirebaseFirestoreException, path: String) {
        exception.printStackTrace()
        when (exception.code) {
            FirebaseFirestoreException.Code.OK -> { }
            FirebaseFirestoreException.Code.CANCELLED -> { }
            FirebaseFirestoreException.Code.NOT_FOUND -> {
                crashReporter.logError(exception, "Accessing $path resulted in not found")
            } // this.onError(DoesntExistError(debugData))
            FirebaseFirestoreException.Code.ALREADY_EXISTS -> {
                crashReporter.logError(exception, "Already exists accessing $path")
            } // this.onError(RecordAlreadyExistsError(debugData))
            FirebaseFirestoreException.Code.PERMISSION_DENIED -> {
                crashReporter.logError(exception, "Permission denied while accessing $path")
            } // this.onError(PermissionError(debugData))
            FirebaseFirestoreException.Code.UNAUTHENTICATED -> {
                crashReporter.logError(exception, "Unauthenticated while accessing $path")
            } // this.onError(UnauthenticatedError(debugData))
            else -> {
                crashReporter.logError(exception, "Unsupported error thrown by Firebase $path")
            } // this.onError(Error("Error occurred ${exception.code}"))
        }
    }
}