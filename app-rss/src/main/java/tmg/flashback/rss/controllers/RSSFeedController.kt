package tmg.flashback.rss.controllers

import tmg.flashback.core.controllers.ConfigurationController
import tmg.flashback.rss.repo.model.SupportedArticleSource
import java.net.MalformedURLException
import java.net.URL

class RSSFeedController(
    val configurationController: ConfigurationController
) {

    /**
     * Get a list of all the supported sources in the app
     */
    private val supportedSources: List<SupportedArticleSource> = configurationController
        .rssSupportedSources
        .map {
            SupportedArticleSource(
                rssLink = it.rssLink,
                sourceShort = it.sourceShort,
                source = it.source,
                colour = it.colour,
                textColour = it.textColour,
                title = it.title,
                contactLink = it.contactLink,
            )
        }

    /**
     * Determine if we should show adding custom rss feeds functionality
     */
    val showAddCustomFeeds: Boolean = configurationController.rssAddCustom

    private val fallbackUrls: Map<String, String> = mapOf(
        "bbc.co.uk" to "https://www.bbc.co.uk",
        "f1i.com" to "https://en.f1i.com"
    )

    val sources: List<SupportedArticleSource> by lazy {
        supportedSources
            .sortedBy { it.rssLink.stripHTTP() }
    }

    fun getSupportedSourceByRssUrl(rssLink: String): SupportedArticleSource? {
        return supportedSources.firstOrNull { it.rssLink == rssLink }
    }

    fun getSupportedSourceByLink(link: String): SupportedArticleSource? {
        return try {
            val url = URL(link)

            supportedSources
                .firstOrNull {
                val supportUrl = URL(it.rssLink)

                url.host.stripWWW() == supportUrl.host.stripWWW()
            } ?: getSupportedSourceByFallbackDomain(url)
        } catch (exception: MalformedURLException) {
            null
        }
    }

    fun stripHost(from: String): String {
        return from.stripHTTP()
    }

    private fun getSupportedSourceByFallbackDomain(url: URL): SupportedArticleSource? {
        val host = url.host.stripWWW()
        val newHostToCheck = fallbackUrls
            .toList()
            .firstOrNull { (override, _) ->
                override == host
            }
            ?.second

        if (newHostToCheck != null) {
            return supportedSources
                .firstOrNull {
                    it.source == newHostToCheck
                }
        }
        return null
    }

    private fun String.stripHTTP(): String {
        return this
            .replace("https://www.", "")
            .replace("http://www.", "")
            .replace("https://", "")
            .replace("http://", "")
            .stripWWW()

    }

    private fun String.stripWWW(): String {
        return when (this.startsWith("www.")) {
            true -> this.substring(4, this.length)
            false -> this
        }
    }
}