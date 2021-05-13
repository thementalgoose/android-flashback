package tmg.flashback.rss.repo

import tmg.core.prefs.manager.PreferenceManager

class RSSRepository(
    private val preferenceManager: PreferenceManager
) {

    companion object {
        private const val keyRssList: String = "RSS_LIST"
        private const val keyRssShowDescription: String = "NEWS_SHOW_DESCRIPTIONS"
        private const val keyInAppEnableJavascript: String = "IN_APP_ENABLE_JAVASCRIPT"
        private const val keyNewsOpenInExternalBrowser: String = "NEWS_OPEN_IN_EXTERNAL_BROWSER"
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