package tmg.flashback.rss.repo

import tmg.flashback.configuration.manager.ConfigManager
import tmg.flashback.rss.repo.json.SupportedSourcesJson
import tmg.flashback.rss.repo.model.SupportedSource
import tmg.flashback.prefs.manager.PreferenceManager
import tmg.flashback.rss.repo.converters.convert

class RSSRepository(
    private val preferenceManager: PreferenceManager,
    private val configManager: ConfigManager
) {

    companion object {

        // Config
        private const val keyRss: String = "rss"
        private const val keyRssAddCustom: String = "rss_add_custom"
        private const val keyRssSupportedSources: String = "rss_supported_sources"

        // Prefs
        private const val keyRssList: String = "RSS_LIST"
        private const val keyRssShowDescription: String = "NEWS_SHOW_DESCRIPTIONS"
        private const val keyInAppEnableJavascript: String = "IN_APP_ENABLE_JAVASCRIPT"
        private const val keyNewsOpenInExternalBrowser: String = "NEWS_OPEN_IN_EXTERNAL_BROWSER"
    }

    /**
     * Is the RSS feature enabled
     */
    val enabled: Boolean by lazy {
        configManager.getBoolean(keyRss)
    }

    /**
     * Is the RSS add custom rss feeds feature enabled
     */
    val addCustom: Boolean by lazy {
        configManager.getBoolean(keyRssAddCustom)
    }

    /**
     * RSS supported sources for the quick add section
     */
    val supportedSources: List<SupportedSource> by lazy {
        configManager
                .getJson<SupportedSourcesJson>(keyRssSupportedSources)
                ?.convert()
                ?: emptyList()
    }

    /**
     * RSS URLs
     */
    var rssUrls: Set<String>
        get() = preferenceManager.getSet(keyRssList, emptySet())
        set(value) = preferenceManager.save(keyRssList, value)

    /**
     * Enable javascript in the in app browser.
     */
    var inAppEnableJavascript: Boolean
        get() = preferenceManager.getBoolean(keyInAppEnableJavascript, true)
        set(value) = preferenceManager.save(keyInAppEnableJavascript, value)

    /**
     * Shows the description for the news items
     */
    var rssShowDescription: Boolean
        get() = preferenceManager.getBoolean(keyRssShowDescription, true)
        set(value) = preferenceManager.save(keyRssShowDescription, value)

    /**
     * Open the news article in an external browser or not
     */
    var newsOpenInExternalBrowser: Boolean
        get() = preferenceManager.getBoolean(keyNewsOpenInExternalBrowser, false)
        set(value) = preferenceManager.save(keyNewsOpenInExternalBrowser, value)
}