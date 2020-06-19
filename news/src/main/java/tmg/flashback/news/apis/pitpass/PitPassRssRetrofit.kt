package tmg.flashback.news.apis.pitpass

import retrofit2.http.GET
import tmg.flashback.news.shared.buildRetrofit

fun buildRetrofitPitPass(isDebug: Boolean = false): PitPassRssRetrofit {
    return buildRetrofit("https://www.pitpass.com",
        isXML = true,
        isDebug = isDebug
    )
}

interface PitPassRssRetrofit {

    @GET(value = "fes_php/fes_usr_sit_newsfeed.php")
    suspend fun getFeed(): PitPassRssModel

}