package tmg.flashback.rss.network.shared

import retrofit2.http.GET
import retrofit2.http.Url
import tmg.flashback.rss.network.apis.model.RssXMLModel

interface RssXMLRetrofit {

    @GET
    suspend fun getRssXML(@Url url: String): RssXMLModel
}