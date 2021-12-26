package tmg.flashback.statistics.network.interceptor

import android.util.Log
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import tmg.flashback.statistics.network.BuildConfig
import tmg.flashback.statistics.network.NetworkConfigManager
import tmg.flashback.statistics.network.manager.BaseUrlLocalOverrideManager

class ConfigUrlInterceptor(
    private val baseUrlLocalOverrideManager: BaseUrlLocalOverrideManager,
    private val networkConfigManager: NetworkConfigManager
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()

        val hostUrl = baseUrlLocalOverrideManager.localBaseUrl ?: networkConfigManager.baseUrl
        if (hostUrl.isEmpty()) { return chain.proceed(request) }

        val newUrl = request.url.toString().replace(networkConfigManager.baseUrl, hostUrl)
        if (BuildConfig.DEBUG) {
            Log.i("OkHttp", "BaseUrlLocalOverrideManager ${baseUrlLocalOverrideManager.localBaseUrl}")
            Log.i("OkHttp", "HostURL $hostUrl")
            Log.i("OkHttp", "URL intercepted from ${request.url} to $newUrl")
        }
        val host = newUrl.toHttpUrlOrNull() ?: return chain.proceed(request)

        Log.i("BaseUrl", "Sending to $host")
        request = request.newBuilder()
            .url(host)
            .build()

        return chain.proceed(request)
    }
}