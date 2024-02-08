package tmg.flashback.flashbacknews.api.interceptors

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

internal class ForceCacheInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        builder.cacheControl(CacheControl.FORCE_CACHE)
        return chain.proceed(builder.build());
    }
}