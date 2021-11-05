package tmg.flashback.statistics.repo.base

import retrofit2.Response
import tmg.crash_reporting.controllers.CrashController
import tmg.flashback.statistics.network.models.MetadataWrapper
import tmg.flashback.statistics.network.utils.data
import tmg.flashback.statistics.network.utils.hasData
import java.lang.RuntimeException

abstract class BaseRepository(
    protected val crashController: CrashController
) {
    suspend fun <T> attempt(
        apiCall: suspend () -> Response<MetadataWrapper<T>>,
        msgIfFailed: String? = null,
        closure: suspend (model: T) -> Boolean
    ): Boolean {
        crashController.log("fetch $msgIfFailed")
        return try {
            val result = apiCall.invoke()
            if (!result.hasData) return false

            val data = result.data() ?: return false
            return closure.invoke(data)
        } catch (e: RuntimeException) {
            crashController.logException(e, msgIfFailed)
            false
        } catch (e: Exception) {
            false
        }
    }
}