package tmg.flashback.rss.network

import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Url
import tmg.flashback.rss.network.model.RssXMLModel

interface RssAPI {

    @GET
    suspend fun getRssXML(@HeaderMap headers: Map<String, String>, @Url url: String): RssXMLModel
}