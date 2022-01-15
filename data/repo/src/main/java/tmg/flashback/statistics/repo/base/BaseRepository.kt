package tmg.flashback.statistics.repo.base

import android.util.Log
import retrofit2.HttpException
import retrofit2.Response
import tmg.flashback.crash_reporting.controllers.CrashController
import tmg.flashback.statistics.network.models.MetadataWrapper
import tmg.flashback.statistics.network.utils.data
import tmg.flashback.statistics.network.utils.hasData
import tmg.flashback.statistics.repo.BuildConfig
import java.net.UnknownHostException

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
        } catch (e: UnknownHostException) {
            if (BuildConfig.DEBUG) {
                Log.d("CrashController", "NOT SENT: Unknown host exception ${e.message}")
                e.printStackTrace()
            }
            false
        } catch (e: HttpException) {
            if (BuildConfig.DEBUG) {
                Log.d("CrashController", "NOT SENT: HTTP Exception thrown ${e.code()} - ${e.message()}")
                e.printStackTrace()
            }
            false
        } catch (e: RuntimeException) {
            crashController.logException(e, msgIfFailed)
            false
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                Log.d("CrashController", "NOT SENT: Other exception thrown ${e.message}")
                e.printStackTrace()
            }
            false
        }
    }
}