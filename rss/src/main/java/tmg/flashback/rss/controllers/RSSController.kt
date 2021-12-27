package tmg.flashback.rss.controllers

import tmg.flashback.appshortcuts.manager.AppShortcutManager
import tmg.flashback.appshortcuts.models.ShortcutInfo
import tmg.flashback.rss.R
import tmg.flashback.rss.repo.RSSRepository
import tmg.flashback.rss.repo.model.SupportedArticleSource
import tmg.flashback.rss.ui.RSSActivity
import java.net.MalformedURLException
import java.net.URL

class RSSController(
    private val rssRepository: RSSRepository,
    private val appShortcutManager: AppShortcutManager
) {

    /**
     * If the feature is enabled or not
     */
    val enabled: Boolean
        get() = rssRepository.enabled

    /**
     * Get a list of all the supported sources in the app
     */
    private val supportedSources: List<SupportedArticleSource>
        get() = rssRepository
            .supportedSources
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
    val showAddCustomFeeds: Boolean
        get() = rssRepository.addCustom


    //region App Shortcuts

    fun addAppShortcut() {
        appShortcutManager.addDynamicShortcut(rssShortcutInfo)
    }

    fun removeAppShortcut() {
        appShortcutManager.removeDynamicShortcut(rssShortcutInfo.id)
    }

    //endregion


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

    companion object {
        private val rssShortcutInfo: ShortcutInfo<RSSActivity> = ShortcutInfo(
            id = "rss",
            shortLabel = R.string.app_shortcut_rss_shorttitle,
            longLabel = R.string.app_shortcut_rss_longtitle,
            icon = R.drawable.app_shortcut_rss,
            unavailableMessage = R.string.app_shortcut_rss_disabled,
            activity = RSSActivity::class,
        )
    }
}