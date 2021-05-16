package tmg.flashback.di_old.rss

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import tmg.flashback.rss.repo.RssAPI
import tmg.flashback.rss.repo.model.Article
import tmg.flashback.rss.repo.model.Response

/**
 * Mock data for RSS feed
 */
internal object MockRSS: RssAPI {
    override fun getNews(): Flow<Response<List<Article>>> = flow {
        emit(Response(listOf(mockRssGoogle)))
    }
}