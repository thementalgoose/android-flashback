package tmg.flashback.flashbacknews.api.api

import androidx.annotation.Keep
import retrofit2.Response
import retrofit2.http.GET
import tmg.flashback.flashbacknews.api.models.MetadataWrapper
import tmg.flashback.flashbacknews.api.models.news.News

@Keep
internal interface FlashbackNewsApi {

    @GET("news.json")
    suspend fun getNews(): Response<MetadataWrapper<List<News>>>
}