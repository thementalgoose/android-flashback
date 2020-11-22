package tmg.flashback.repo.db.news

import kotlinx.coroutines.flow.Flow
import tmg.flashback.repo.models.Response
import tmg.flashback.repo.models.rss.Article

interface RSSDB {
    fun getNews(): Flow<Response<List<Article>>>
}