package tmg.flashback.statistics.repo.base

import retrofit2.Response
import tmg.crash_reporting.controllers.CrashController
import tmg.flashback.statistics.network.models.MetadataWrapper
import tmg.flashback.statistics.network.utils.data
import tmg.flashback.statistics.network.utils.hasData
import java.lang.RuntimeException

abstract class BaseRepository(
    val crashController: CrashController
) {
//    fun <T> attempt(msgIfFailed: String? = null, closure: () -> T?): T? {
//        return try {
//            closure.invoke()
//        } catch (e: RuntimeException) {
//            crashController.logException(e, msgIfFailed)
//            null
//        }
//    }

    suspend fun <T> attempt(
        apiCall: suspend () -> Response<MetadataWrapper<T>>,
        msgIfFailed: String? = null,
        closure: suspend (model: T) -> Boolean
    ): Boolean {
        return try {
            val result = apiCall.invoke()
            if (!result.hasData) return false

            val data = result.data() ?: return false
            return closure.invoke(data)
        } catch (e: RuntimeException) {
            crashController.logException(e, msgIfFailed)
            false
        }
    }

    suspend fun attempt(msgIfFailed: String? = null, closure: suspend () -> Boolean): Boolean {
        return try {
            val result = closure.invoke()
            result
        } catch (e: RuntimeException) {
            crashController.logException(e, msgIfFailed)
            false
        }
    }
}