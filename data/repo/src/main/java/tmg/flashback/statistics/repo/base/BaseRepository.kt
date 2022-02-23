package tmg.flashback.statistics.repo.base

import android.util.Log
import retrofit2.HttpException
import retrofit2.Response
import tmg.flashback.crash_reporting.controllers.CrashController
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.statistics.network.NetworkConfigManager
import tmg.flashback.statistics.network.models.MetadataWrapper
import tmg.flashback.statistics.network.utils.data
import tmg.flashback.statistics.network.utils.hasData
import tmg.flashback.statistics.repo.BuildConfig
import java.io.IOException
import java.net.UnknownHostException

abstract class BaseRepository(
    protected val crashController: CrashController,
    private val networkConnectivityManager: NetworkConnectivityManager
) {
    suspend fun <T> attempt(
        apiCall: suspend () -> Response<MetadataWrapper<T>>,
        msgIfFailed: String? = null,
        closure: suspend (model: T) -> Boolean
    ): Boolean {
        crashController.log("fetch $msgIfFailed isConnected=${networkConnectivityManager.isConnected}")
        return try {
            val result = apiCall.invoke()
            if (!result.hasData) return false

            val data = result.data() ?: return false
            return closure.invoke(data)
        } catch (e: UnknownHostException) {
            crashController.log("fetch $msgIfFailed UnknownHostException")
            if (BuildConfig.DEBUG) {
                Log.d("CrashController", "NOT SENT: Unknown host exception ${e.message}")
                e.printStackTrace()
            }
            false
        } catch (e: IOException) {
            crashController.log("fetch $msgIfFailed IOException ${e.javaClass.simpleName}")
            if (BuildConfig.DEBUG) {
                Log.d("CrashController", "NOT SENT: IOException ${e.message}")
                e.printStackTrace()
            }
            false
        } catch (e: HttpException) {
            crashController.log("fetch $msgIfFailed HttpException")
            if (BuildConfig.DEBUG) {
                Log.d("CrashController", "NOT SENT: HTTP Exception thrown ${e.code()} - ${e.message()}")
                e.printStackTrace()
            }
            false
        } catch (e: RuntimeException) {
            crashController.logException(e, msgIfFailed)
            if (BuildConfig.DEBUG) {
                Log.d("CrashController", "Other exception thrown ${e.message}")
                e.printStackTrace()
            }
            false
        } catch (e: Exception) {
            crashController.logException(e, msgIfFailed)
            if (BuildConfig.DEBUG) {
                Log.d("CrashController", "Other exception thrown ${e.message}")
                e.printStackTrace()
            }
            false
        }
    }
}