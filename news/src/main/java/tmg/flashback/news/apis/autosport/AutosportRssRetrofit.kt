package tmg.flashback.news.apis.autosport

import retrofit2.http.GET
import tmg.flashback.news.shared.buildRetrofit

fun buildRetrofitAutosport(isDebug: Boolean = false): AutosportRssRetrofit {
    return buildRetrofit("https://www.autosport.com",
        isXML = true,
        isDebug = isDebug
    )
}

interface AutosportRssRetrofit {

    @GET(value = "rss/feed/f1")
    suspend fun getFeed(): AutosportRssModel

}