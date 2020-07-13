package tmg.flashback.news.apis.motorsport

import retrofit2.http.GET
import tmg.flashback.news.shared.buildRetrofit


fun buildRetrofitMotorsport(isDebug: Boolean = false): MotorsportRssRetrofit {
    return buildRetrofit("https://motorsport.com",
        isXML = true,
        isDebug = isDebug
    )
}

interface MotorsportRssRetrofit {

    @GET(value = "rss/f1/news/")
    suspend fun getFeed(): MotorsportRssModel
}