package tmg.flashback.rss.shared

import retrofit2.http.GET
import retrofit2.http.Url
import tmg.flashback.rss.apis.model.RssXMLModel

interface RssXMLRetrofit {

    @GET
    suspend fun getRssXML(@Url url: String): RssXMLModel
}