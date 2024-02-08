package tmg.flashback.flashbackapi.api.api

import androidx.annotation.Keep
import retrofit2.Response
import retrofit2.http.GET
import tmg.flashback.flashbackapi.api.models.MetadataWrapper
import tmg.flashback.flashbackapi.api.models.news.News

@Keep
interface FlashbackNewsApi {

    @GET("news.json")
    suspend fun getNews(): Response<MetadataWrapper<List<News>>>
}