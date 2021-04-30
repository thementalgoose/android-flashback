package tmg.flashback.rss.prefs

interface RSSRepository {

    /**
     * RSS URLs
     */
    var rssUrls: Set<String>

    /**
     * Enable javascript in the in app browser.
     */
    var inAppEnableJavascript: Boolean

    /**
     * Shows the description for the news items
     */
    var rssShowDescription: Boolean

    /**
     * Open the news article in an external browser or not
     */
    var newsOpenInExternalBrowser: Boolean
}