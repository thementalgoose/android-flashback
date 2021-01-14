package tmg.flashback.rss.controllers

import tmg.flashback.rss.repo.enums.SupportedArticleSource
import java.net.MalformedURLException
import java.net.URL

abstract class RSSController {

    /**
     * Get a list of all the supported sources in the app
     */
    protected abstract val supportedSources: List<SupportedArticleSource>

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
            } ?: null // TODO: Fix this! SupportedArticleSource.findFallback(url)
        } catch (exception: MalformedURLException) {
            null
        }
    }

    fun stripHost(from: String): String {
        return from.stripHTTP()
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