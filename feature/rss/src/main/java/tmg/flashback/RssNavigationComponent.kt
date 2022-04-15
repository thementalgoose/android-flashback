package tmg.flashback

import android.content.Context
import android.content.Intent
import tmg.flashback.rss.ui.RSSActivity
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
}