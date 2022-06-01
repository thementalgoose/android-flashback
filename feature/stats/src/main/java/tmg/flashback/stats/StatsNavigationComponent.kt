package tmg.flashback.stats

import android.content.Context
import android.content.Intent
import tmg.flashback.stats.ui.weekend.WeekendActivity
import tmg.flashback.stats.ui.weekend.WeekendInfo
import tmg.flashback.ui.navigation.ActivityProvider

class StatsNavigationComponent(
    private val activityProvider: ActivityProvider
) {
    fun weekendIntent(context: Context, weekendInfo: WeekendInfo): Intent {
        return WeekendActivity.intent(context, weekendInfo)
    }

    fun weekend(weekendInfo: WeekendInfo) = activityProvider.launch {
        val intent = weekendIntent(it, weekendInfo)
        it.startActivity(intent)
    }
}