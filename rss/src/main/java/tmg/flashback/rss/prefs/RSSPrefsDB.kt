package tmg.flashback.rss.prefs

import tmg.flashback.repo.enums.ThemePref

interface RSSPrefsDB {

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

    /**
     * Current theme
     */
    val theme: ThemePref
}