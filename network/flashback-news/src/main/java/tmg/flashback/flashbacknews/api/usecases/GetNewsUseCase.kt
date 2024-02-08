package tmg.flashback.flashbacknews.api.usecases

import tmg.flashback.flashbacknews.api.models.news.News

interface GetNewsUseCase {
    suspend fun getNews(): List<News>?
}