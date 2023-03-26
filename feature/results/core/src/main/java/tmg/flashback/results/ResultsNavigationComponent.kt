package tmg.flashback.results

import androidx.appcompat.app.AppCompatActivity
import tmg.flashback.crash_reporting.manager.CrashManager
import tmg.flashback.navigation.NavigationDestination
import tmg.flashback.navigation.Screen
import tmg.flashback.results.contract.ResultsNavigationComponent
import tmg.flashback.results.ui.events.EventsBottomSheetFragment
import tmg.flashback.results.ui.feature.notificationonboarding.NotificationOnboardingBottomSheetFragment
import tmg.flashback.results.ui.settings.notifications.reminder.UpNextReminderBottomSheetFragment
import tmg.flashback.results.ui.tyres.TyreBottomSheetFragment
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

internal class ResultsNavigationComponentImpl @Inject constructor(
    private val crashManager: CrashManager,
    private val activityProvider: tmg.flashback.navigation.ActivityProvider
): ResultsNavigationComponent {
    override fun upNext() = activityProvider.launch {
        crashManager.log("Navigating to up next")
        val activity = it as? AppCompatActivity ?: return@launch
        UpNextReminderBottomSheetFragment().show(activity.supportFragmentManager, "UP_NEXT")
    }

    override fun tyres(season: Int) = activityProvider.launch {
        crashManager.log("Navigating to tyres £$season")
        val activity = it as? AppCompatActivity ?: return@launch
        TyreBottomSheetFragment.instance(season).show(activity.supportFragmentManager, "TYRES")
    }

    override fun preseasonEvents(season: Int) = activityProvider.launch {
        crashManager.log("Navigating to preseason £$season")
        val activity = it as? AppCompatActivity ?: return@launch
        EventsBottomSheetFragment.instance(season).show(activity.supportFragmentManager, "PRESEASON")
    }

    override fun featureNotificationOnboarding() = activityProvider.launch {
        crashManager.log("Navigating to notification onboarding")
        val activity = it as? AppCompatActivity ?: return@launch
        NotificationOnboardingBottomSheetFragment.instance().show(activity.supportFragmentManager, "FEATURE_NOTIFICATIONS")
    }
}