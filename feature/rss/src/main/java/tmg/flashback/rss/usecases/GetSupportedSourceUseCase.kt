package tmg.flashback.rss.usecases

import tmg.flashback.rss.repo.RSSRepository
import tmg.flashback.rss.repo.converters.toArticleSource
import tmg.flashback.rss.repo.model.SupportedArticleSource
import tmg.flashback.rss.utils.stripHTTP
import tmg.flashback.rss.utils.stripWWW
import java.net.MalformedURLException
import java.net.URL
import javax.inject.Inject

class GetSupportedSourceUseCase @Inject constructor(
    private val rssRepository: RSSRepository,
) {

    private val fallbackUrls: Map<String, String> = mapOf(
        "bbc.co.uk" to "https://www.bbc.co.uk",
        "f1i.com" to "https://en.f1i.com"
    )

    private val all get() = rssRepository
        .supportedSources
        .map { it.toArticleSource() }
        .sortedBy { it.rssLink.stripHTTP() }

    fun getByRssLink(rssLink: String): SupportedArticleSource? {
        return all.firstOrNull { it.rssLink == rssLink }
    }

    fun getByLink(link: String): SupportedArticleSource? {
        return try {
            val url = URL(link)

            all.firstOrNull {
                val supportUrl = URL(it.rssLink)

                url.host.stripWWW() == supportUrl.host.stripWWW()
            } ?: getSupportedSourceByFallbackDomain(url)
        } catch (exception: MalformedURLException) {
            null
        }
    }

    private fun getSupportedSourceByFallbackDomain(url: URL): SupportedArticleSource? {
        val host = url.host.stripWWW()
        println("HOST $host")
        val newHostToCheck = fallbackUrls
            .toList()
            .firstOrNull { (override, _) ->
                override == host
            }
            ?.second

        if (newHostToCheck != null) {
            return all
                .firstOrNull {
                    it.source == newHostToCheck
                }
        }
        return null
    }
}