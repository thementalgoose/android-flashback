package tmg.flashback.stats

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import tmg.flashback.stats.ui.circuits.CircuitActivity
import tmg.flashback.stats.ui.search.SearchActivity
import tmg.flashback.stats.ui.settings.home.SettingsHomeActivity
import tmg.flashback.stats.ui.settings.notifications.SettingsNotificationsActivity
import tmg.flashback.stats.ui.settings.notifications.reminder.UpNextReminderBottomSheetFragment
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

    internal fun upNext() = activityProvider.launch {
        val activity = it as? AppCompatActivity ?: return@launch
        UpNextReminderBottomSheetFragment().show(activity.supportFragmentManager, "UP_NEXT")
    }

    internal fun settingsHomeIntent(context: Context): Intent {
        return Intent(context, SettingsHomeActivity::class.java)
    }
    fun settingsHome() = activityProvider.launch {
        it.startActivity(settingsHomeIntent(it))
    }

    internal fun settingsNotificationsIntent(context: Context): Intent {
        return Intent(context, SettingsNotificationsActivity::class.java)
    }
    fun settingsNotifications() = activityProvider.launch {
        it.startActivity(settingsNotificationsIntent(it))
    }
}