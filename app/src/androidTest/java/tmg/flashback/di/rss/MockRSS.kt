package tmg.flashback.di.rss

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import tmg.flashback.repo.models.Response
import tmg.flashback.rss.repo.RSSRepository
import tmg.flashback.rss.repo.model.Article

/**
 * Mock data for RSS feed
 */
internal object MockRSS: RSSRepository {
    override fun getNews(): Flow<Response<List<Article>>> = flow {
        emit(Response(listOf(mockRssGoogle)))
    }
}