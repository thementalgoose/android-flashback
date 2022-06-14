package tmg.flashback

import android.content.Context
import android.content.Intent
import tmg.flashback.rss.ui.feed.RSSActivity
import tmg.flashback.rss.ui.settings.InitialScreen
import tmg.flashback.rss.ui.settings.RSSSettingsActivity
import tmg.flashback.rss.ui.web.WebActivity
import tmg.flashback.ui.navigation.ActivityProvider

class RssNavigationComponent(
    private val activityProvider: ActivityProvider
) {
    fun rssIntent(context: Context): Intent {
        return RSSActivity.intent(context)
    }

    fun rss() = activityProvider.launch {
        it.startActivity(rssIntent(it))
    }

    internal fun rssSettingsIntent(context: Context, screen: InitialScreen = InitialScreen.CONFIGURE): Intent {
        return RSSSettingsActivity.intent(context, screen)
    }

    internal fun rssSettings(screen: InitialScreen = InitialScreen.CONFIGURE) = activityProvider.launch {
        it.startActivity(rssSettingsIntent(it, screen))
    }

    internal fun webIntent(context: Context, title: String, url: String): Intent {
        return WebActivity.intent(context, title, url)
    }

    internal fun web(title: String, url: String) = activityProvider.launch {
        it.startActivity(webIntent(it, title, url))
    }
}