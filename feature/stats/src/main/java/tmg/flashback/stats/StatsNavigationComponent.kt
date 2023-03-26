package tmg.flashback.stats

import androidx.appcompat.app.AppCompatActivity
import tmg.flashback.crash_reporting.manager.CrashManager
import tmg.flashback.navigation.NavigationDestination
import tmg.flashback.navigation.Screen
import tmg.flashback.stats.ui.events.EventsBottomSheetFragment
import tmg.flashback.stats.ui.feature.notificationonboarding.NotificationOnboardingBottomSheetFragment
import tmg.flashback.stats.ui.settings.notifications.reminder.UpNextReminderBottomSheetFragment
import tmg.flashback.stats.ui.tyres.TyreBottomSheetFragment
import tmg.flashback.weekend.contract.model.WeekendInfo
import javax.inject.Inject

@JvmInline
value class ScreenCalendar(val route: String)
val Screen.Calendar get() = ScreenCalendar("results/calendar/{season}")
fun ScreenCalendar.with(
    season: Int
) = NavigationDestination(
    route = this@with.route.replace("{season}", season.toString()),
    launchSingleTop = true
)


@JvmInline
value class ScreenDrivers(val route: String)
val Screen.Drivers get() = ScreenDrivers("results/drivers/{season}")
fun ScreenDrivers.with(
    season: Int
) = NavigationDestination(
    route = this@with.route.replace("{season}", season.toString()),
    launchSingleTop = true
)


@JvmInline
value class ScreenConstructors(val route: String)
val Screen.Constructors get() = ScreenCalendar("results/constructors/{season}")
fun ScreenConstructors.with(
    season: Int
) = NavigationDestination(
    route = this@with.route.replace("{season}", season.toString()),
    launchSingleTop = true
)

class StatsNavigationComponent @Inject constructor(
    private val crashManager: CrashManager,
    private val activityProvider: tmg.flashback.navigation.ActivityProvider
) {
    fun upNext() = activityProvider.launch {
        crashManager.log("Navigating to up next")
        val activity = it as? AppCompatActivity ?: return@launch
        UpNextReminderBottomSheetFragment().show(activity.supportFragmentManager, "UP_NEXT")
    }

    internal fun tyres(season: Int) = activityProvider.launch {
        crashManager.log("Navigating to tyres £$season")
        val activity = it as? AppCompatActivity ?: return@launch
        TyreBottomSheetFragment.instance(season).show(activity.supportFragmentManager, "TYRES")
    }

    internal fun preseason(season: Int) = activityProvider.launch {
        crashManager.log("Navigating to preseason £$season")
        val activity = it as? AppCompatActivity ?: return@launch
        EventsBottomSheetFragment.instance(season).show(activity.supportFragmentManager, "PRESEASON")
    }

    fun featureNotificationOnboarding() = activityProvider.launch {
        crashManager.log("Navigating to notification onboarding")
        val activity = it as? AppCompatActivity ?: return@launch
        NotificationOnboardingBottomSheetFragment.instance().show(activity.supportFragmentManager, "FEATURE_NOTIFICATIONS")
    }
}