package tmg.flashback

import android.content.Intent
import tmg.flashback.rss.ui.RSSActivity
import tmg.flashback.ui.navigation.ActivityProvider

class RssNavigationComponent(
    private val activityProvider: ActivityProvider
) {
    fun rssLaunch() {
        val activity = activityProvider.activity ?: return
        activity.startActivity(RSSActivity.intent(activity))
    }

    fun rssIntent(): Intent? {
        val activity = activityProvider.activity ?: return null
        return Intent(activity, RSSActivity::class.java)
    }
}