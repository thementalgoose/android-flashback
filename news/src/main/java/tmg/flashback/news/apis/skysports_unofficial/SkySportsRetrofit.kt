package tmg.flashback.news.apis.skysports_unofficial

import retrofit2.http.GET
import tmg.flashback.news.shared.buildRetrofit

fun buildRetrofitSkySports(isDebug: Boolean = false): SkySportsRssRetrofit {
    return buildRetrofit("https://skysportsapi.herokuapp.com/",
        isXML = false,
        isDebug = isDebug
    )
}

interface SkySportsRssRetrofit {

    @GET(value = "sky/getnews/f1/v1.0")
    suspend fun getFeed(): List<SkySportsModel>
}