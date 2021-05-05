package tmg.flashback.rss.repo

import tmg.flashback.device.repository.SharedPreferenceRepository

class RSSRepository(
    private val sharedPreferenceRepository: SharedPreferenceRepository
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
        get() = sharedPreferenceRepository.getSet(keyRssList, emptySet())
        set(value) = sharedPreferenceRepository.save(keyRssList, value)

    /**
     * Enable javascript in the in app browser.
     */
    var inAppEnableJavascript: Boolean
        get() = sharedPreferenceRepository.getBoolean(keyInAppEnableJavascript, true)
        set(value) = sharedPreferenceRepository.save(keyInAppEnableJavascript, value)

    /**
     * Shows the description for the news items
     */
    var rssShowDescription: Boolean
        get() = sharedPreferenceRepository.getBoolean(keyRssShowDescription, true)
        set(value) = sharedPreferenceRepository.save(keyRssShowDescription, value)

    /**
     * Open the news article in an external browser or not
     */
    var newsOpenInExternalBrowser: Boolean
        get() = sharedPreferenceRepository.getBoolean(keyNewsOpenInExternalBrowser, false)
        set(value) = sharedPreferenceRepository.save(keyNewsOpenInExternalBrowser, value)
}