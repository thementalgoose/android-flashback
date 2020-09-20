package tmg.flashback.news.apis.racefans

import retrofit2.http.GET
import tmg.flashback.news.shared.buildRetrofit

fun buildRetrofitRaceFans(isDebug: Boolean = false): RaceFansRssRetrofit {
    return buildRetrofit("https://racefans.net",
        isXML = true,
        isDebug = isDebug
    )
}

interface RaceFansRssRetrofit {

    @GET(value = "feed/")
    suspend fun getFeed(): RaceFansRssModel
}