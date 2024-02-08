package tmg.flashback.flashbacknews.api.usecases

import tmg.flashback.flashbacknews.api.api.FlashbackNewsApi
import tmg.flashback.flashbacknews.api.models.news.News
import tmg.flashback.flashbacknews.api.utils.data
import javax.inject.Inject

internal class GetNewsUseCaseImpl @Inject constructor(
    private val flashbackNewsApi: FlashbackNewsApi
): GetNewsUseCase {

    override suspend fun getNews(): List<News>? {
        try {
            val response = flashbackNewsApi.getNews()
            if (response.isSuccessful) {
                val news = response.data() ?: emptyList()
                return news
            } else {
                return null
            }
        } catch (e: retrofit2.HttpException) {
            return null
        } catch (e: RuntimeException) {
            return null
        } catch (e: Exception) {
            return null
        }
    }
}