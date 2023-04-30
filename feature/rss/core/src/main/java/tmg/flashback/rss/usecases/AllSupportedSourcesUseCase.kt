package tmg.flashback.rss.usecases

import tmg.flashback.rss.repo.RssRepository
import tmg.flashback.rss.repo.converters.toArticleSource
import tmg.flashback.rss.repo.model.SupportedArticleSource
import tmg.flashback.rss.utils.stripHTTP
import javax.inject.Inject

class AllSupportedSourcesUseCase @Inject constructor(
    private val rssRepository: RssRepository
) {
    fun getSources(): List<SupportedArticleSource> {
        if (!rssRepository.enabled) return emptyList()
        return rssRepository
            .supportedSources
            .map { it.toArticleSource() }
            .sortedBy { it.rssLink.stripHTTP() }
    }
}