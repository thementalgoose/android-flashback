package tmg.flashback.news.apis.crashnet

import retrofit2.http.GET
import tmg.flashback.news.shared.buildRetrofit

fun buildRetrofitCrashNet(isDebug: Boolean = false): CrashNetRssRetrofit {
    return buildRetrofit("https://crash.net",
        isXML = true,
        isDebug = isDebug
    )
}

interface CrashNetRssRetrofit {

    @GET(value = "rss/f1")
    suspend fun getFeed(): CrashNetRssModel
}