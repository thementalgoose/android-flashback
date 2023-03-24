package tmg.flashback.rss.network.shared

import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Url
import tmg.flashback.rss.network.apis.model.RssXMLModel

internal interface RssXMLRetrofit {

    @GET
    suspend fun getRssXML(@HeaderMap headers: Map<String, String>, @Url url: String): RssXMLModel
}