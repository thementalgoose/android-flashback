package tmg.flashback.rss.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody

object CleanupXmlInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if (response.isSuccessful) {
            val stringBody = response.body!!.string()
                    .replace("<head/>", "")
            return response
                    .newBuilder()
                    .body(stringBody.toResponseBody())
                    .build()
        }
        return response
    }
}