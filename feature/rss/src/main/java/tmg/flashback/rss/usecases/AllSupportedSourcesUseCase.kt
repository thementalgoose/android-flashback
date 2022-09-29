package tmg.flashback.rss.usecases

import tmg.flashback.rss.repo.RSSRepository
import tmg.flashback.rss.repo.converters.toArticleSource
import tmg.flashback.rss.repo.model.SupportedArticleSource
import tmg.flashback.rss.utils.stripHTTP
import javax.inject.Inject

class AllSupportedSourcesUseCase @Inject constructor(
    private val rssRepository: RSSRepository
) {
    fun getSources(): List<SupportedArticleSource> {
        return rssRepository
            .supportedSources
            .map { it.toArticleSource() }
            .sortedBy { it.rssLink.stripHTTP() }
    }
}