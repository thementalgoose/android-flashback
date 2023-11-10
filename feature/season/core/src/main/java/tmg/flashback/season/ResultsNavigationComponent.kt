package tmg.flashback.season

import androidx.appcompat.app.AppCompatActivity
import tmg.flashback.crashlytics.manager.CrashlyticsManager
import tmg.flashback.device.ActivityProvider
import tmg.flashback.season.contract.ResultsNavigationComponent
import tmg.flashback.season.presentation.events.EventsBottomSheetFragment
import tmg.flashback.season.presentation.tyres.TyreBottomSheetFragment
import javax.inject.Inject

//@JvmInline
//value class ScreenCalendar(val route: String)
//val Screen.Calendar get() = ScreenCalendar("results/calendar/{season}")
//fun ScreenCalendar.with(
//    season: Int?
//) = NavigationDestination(
//    route = this@with.route.replace("{season}", season?.toString() ?: "current"),
//    launchSingleTop = true
//)


//@JvmInline
//value class ScreenDrivers(val route: String)
//val Screen.Drivers get() = ScreenDrivers("results/drivers/{season}")
//fun ScreenDrivers.with(
//    season: Int?
//) = NavigationDestination(
//    route = this@with.route.replace("{season}", season?.toString() ?: "current"),
//    launchSingleTop = true
//)


//@JvmInline
//value class ScreenConstructors(val route: String)
//val Screen.Constructors get() = ScreenCalendar("results/constructors/{season}")
//fun ScreenConstructors.with(
//    season: Int?
//) = NavigationDestination(
//    route = this@with.route.replace("{season}", season?.toString() ?: "current"),
//    launchSingleTop = true
//)

internal class ResultsNavigationComponentImpl @Inject constructor(
    private val crashlyticsManager: CrashlyticsManager,
    private val activityProvider: ActivityProvider
): ResultsNavigationComponent {

    override fun tyres(season: Int) = activityProvider.launch {
        crashlyticsManager.log("Navigating to tyres $season")
        val activity = it as? AppCompatActivity ?: return@launch
        TyreBottomSheetFragment.instance(season).show(activity.supportFragmentManager, "TYRES")
    }

    override fun preseasonEvents(season: Int) = activityProvider.launch {
        crashlyticsManager.log("Navigating to preseason $season")
        val activity = it as? AppCompatActivity ?: return@launch
        EventsBottomSheetFragment.instance(season).show(activity.supportFragmentManager, "PRESEASON")
    }
}