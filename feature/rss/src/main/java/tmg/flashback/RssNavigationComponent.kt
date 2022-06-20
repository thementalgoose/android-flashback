package tmg.flashback

import android.content.Context
import android.content.Intent
import tmg.flashback.rss.ui.configure.ConfigureRSSActivity
import tmg.flashback.rss.ui.feed.RSSActivity
import tmg.flashback.rss.ui.settings.SettingsRSSActivity
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

    fun settingsRSSIntent(context: Context): Intent {
        return SettingsRSSActivity.intent(context)
    }

    fun settingsRSS() = activityProvider.launch {
        it.startActivity(settingsRSSIntent(it))
    }

    internal fun configureRSSIntent(context: Context): Intent {
        return ConfigureRSSActivity.intent(context)
    }

    internal fun configureRSS() = activityProvider.launch {
        it.startActivity(configureRSSIntent(it))
    }

    internal fun webIntent(context: Context, title: String, url: String): Intent {
        return WebActivity.intent(context, title, url)
    }

    internal fun web(title: String, url: String) = activityProvider.launch {
        it.startActivity(webIntent(it, title, url))
    }
}