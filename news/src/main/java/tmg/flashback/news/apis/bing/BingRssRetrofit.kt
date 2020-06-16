package tmg.flashback.news.apis.bing

import retrofit2.http.GET
import tmg.flashback.news.shared.buildRetrofit

fun buildRetrofitAutosport(isDebug: Boolean = false): AutosportRssRetrofit {
    return buildRetrofit("https://bing.com",
        isXML = true,
        isDebug = isDebug
    )
}

interface AutosportRssRetrofit {

    @GET(value = "news/search?q=formula+1&format=rss")
    suspend fun getFeed(): BingRssModel

}