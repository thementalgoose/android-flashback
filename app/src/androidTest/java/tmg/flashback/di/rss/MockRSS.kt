package tmg.flashback.di.rss

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import tmg.flashback.data.models.Response
import tmg.flashback.rss.repo.RssAPI
import tmg.flashback.rss.repo.model.Article

/**
 * Mock data for RSS feed
 */
internal object MockRSS: RssAPI {
    override fun getNews(): Flow<Response<List<Article>>> = flow {
        emit(Response(listOf(mockRssGoogle)))
    }
}