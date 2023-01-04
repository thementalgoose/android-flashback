package tmg.flashback.rss.repo

import kotlinx.coroutines.flow.Flow
import tmg.flashback.rss.repo.model.Article
import tmg.flashback.rss.repo.model.Response

interface RssAPI {
    fun getNews(): Flow<Response<List<Article>>>
}