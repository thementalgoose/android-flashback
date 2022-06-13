package tmg.flashback.stats

import android.content.Context
import android.content.Intent
import tmg.flashback.stats.ui.circuits.CircuitActivity
import tmg.flashback.stats.ui.search.SearchActivity
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

    fun searchIntent(context: Context): Intent {
        return SearchActivity.intent(context)
    }

    fun search() = activityProvider.launch {
        val intent = searchIntent(it)
        it.startActivity(intent)
    }

    fun circuitIntent(context: Context, circuitId: String, circuitName: String): Intent {
        return CircuitActivity.intent(
            context = context,
            circuitId = circuitId,
            circuitName = circuitName
        )
    }

    fun circuit(circuitId: String, circuitName: String) = activityProvider.launch {
        val intent = circuitIntent(
            context = it,
            circuitId = circuitId,
            circuitName = circuitName
        )
        it.startActivity(intent)
    }
}