package tmg.flashback.rss.controllers

import android.util.Log
import tmg.flashback.rss.repo.enums.SupportedArticleSource
import java.net.MalformedURLException
import java.net.URL

abstract class RSSController {

    /**
     * Get a list of all the supported sources in the app
     */
    protected abstract val supportedSources: List<SupportedArticleSource>

    /**
     * Determine if we should show adding custom rss feeds functionality
     */
    abstract val showAddCustomFeeds: Boolean

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