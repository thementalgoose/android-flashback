package tmg.flashback.repo.db.news

import kotlinx.coroutines.flow.Flow
import tmg.flashback.repo.models.Response
import tmg.flashback.repo.models.news.Article

interface NewsDB {
    fun syncAll()
    fun getNews(): Flow<Response<List<Article>>>
}