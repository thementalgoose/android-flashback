package tmg.flashback.statistics.network.interceptor

import android.util.Log
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import tmg.flashback.statistics.network.BuildConfig
import tmg.flashback.statistics.network.NetworkConfigManager
import tmg.flashback.statistics.network.manager.BaseUrlLocalOverrideManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConfigUrlInterceptor @Inject constructor(
    private val baseUrlLocalOverrideManager: BaseUrlLocalOverrideManager,
    private val networkConfigManager: NetworkConfigManager
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()

        // Local override
        baseUrlLocalOverrideManager.localBaseUrl?.let { url ->
            log("Local override url found of $url, overriding...")
            val newUrl = request.url.toString().replace(networkConfigManager.defaultBaseUrl, url)
            val host = newUrl.toHttpUrlOrNull() ?: return chain.proceed(request)
            request = request.newBuilder()
                .url(host)
                .build()

            return chain.proceed(request)
        }

        log("No local override found, continuing..")

        // Default
        val hostUrl = networkConfigManager.baseUrl
        log("Config url found of $hostUrl, default url found of ${request.url}")
        if (hostUrl.isEmpty()) { return chain.proceed(request) }

        val newUrl = request.url.toString().replace(networkConfigManager.defaultBaseUrl, hostUrl)
        log("Changing ${request.url} -> $newUrl")
        val host = newUrl.toHttpUrlOrNull() ?: return chain.proceed(request)

        Log.i("Network", "Requesting $newUrl")
        request = request.newBuilder()
            .url(host)
            .build()

        return chain.proceed(request)
    }

    private fun log(msg: String) {
        if (BuildConfig.DEBUG) {
            Log.i("OkHttp", msg)
        }
    }
}