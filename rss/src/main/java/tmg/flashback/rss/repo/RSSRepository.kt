package tmg.flashback.rss.repo

import kotlinx.coroutines.flow.Flow
import tmg.flashback.repo.models.Response
import tmg.flashback.rss.repo.model.Article

interface RSSRepository {
    fun getNews(): Flow<Response<List<Article>>>
}