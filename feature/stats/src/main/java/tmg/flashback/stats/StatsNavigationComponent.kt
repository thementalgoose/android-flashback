package tmg.flashback.stats

import androidx.appcompat.app.AppCompatActivity
import tmg.flashback.stats.ui.feature.notificationonboarding.NotificationOnboardingBottomSheetFragment
import tmg.flashback.stats.ui.settings.notifications.reminder.UpNextReminderBottomSheetFragment
import tmg.flashback.stats.ui.tyres.TyreBottomSheetFragment
import tmg.flashback.stats.ui.weekend.WeekendInfo
import tmg.flashback.ui.navigation.ActivityProvider
import tmg.flashback.ui.navigation.NavigationDestination
import tmg.flashback.ui.navigation.Navigator
import tmg.flashback.ui.navigation.Screen

val Screen.DriverPlaceholder: String get() = "drivers/{driverId}"
fun Screen.Driver(driverId: String): NavigationDestination = object : NavigationDestination {
    override val route: String = "drivers/$driverId"
}

val Screen.DriverSeasonPlaceholder: String get() = "drivers/{driverId}/{season}"
fun Screen.Driver(driverId: String, season: Int): NavigationDestination = object : NavigationDestination {
    override val route: String = "drivers/$driverId/$season"
}

val ConstructorPlaceholder: String get() = "constructors/{constructorId}"
fun Screen.Constructor(constructorId: String): NavigationDestination = object : NavigationDestination {
    override val route: String = "constructors/$constructorId"
}

val Screen.WeekendPlaceholder: String get() = "weekend/{season}/{round}"
fun Screen.Weekend(season: Int, round: Int): NavigationDestination = object : NavigationDestination {
    override val route: String = "weekend/$season/$round"
}

val Screen.CircuitPlaceholder: String get() = "circuit/{circuitId}"
fun Screen.Circuit(circuitId: String): NavigationDestination = object : NavigationDestination {
    override val route: String = "circuit/$circuitId"
}

val Screen.Settings.Home: NavigationDestination
    get() = object : NavigationDestination {
        override val route: String = "settings/home"
    }

val Screen.Settings.Notifications: NavigationDestination
    get() = object : NavigationDestination {
        override val route: String = "settings/notifications"
    }

val Screen.Search: NavigationDestination
    get() = object : NavigationDestination {
        override val route: String = "search"
    }

class StatsNavigationComponent(
    private val navigator: Navigator,
    private val activityProvider: ActivityProvider
) {
    fun driverOverview(id: String, name: String) {
        navigator.navigate(Screen.Driver(
            driverId = id
        ))
    }

    fun driverSeason(id: String, name: String, season: Int) {
        navigator.navigate(Screen.Driver(
            driverId = id,
            season = season
        ))
    }

    fun constructorOverview(id: String, name: String) {
        navigator.navigate(Screen.Constructor(
            constructorId = id
        ))
    }

    @Deprecated("Not yet implemented!")
    fun constructorSeason(id: String, name: String, season: Int) {
        TODO()
    }

    fun weekend(weekendInfo: WeekendInfo) {
        navigator.navigate(Screen.Weekend(
            season = weekendInfo.season,
            round = weekendInfo.round
        ))
    }

    fun search() {
        navigator.navigate(Screen.Search)
    }

    fun circuit(circuitId: String, circuitName: String) {
        navigator.navigate(Screen.Circuit(
            circuitId = circuitId
        ))
    }


    fun settingsHome() {
        navigator.navigate(Screen.Settings.Home)
    }

    fun settingsNotifications() {
        navigator.navigate(Screen.Settings.Notifications)
    }

    internal fun upNext() = activityProvider.launch {
        val activity = it as? AppCompatActivity ?: return@launch
        UpNextReminderBottomSheetFragment().show(activity.supportFragmentManager, "UP_NEXT")
    }

    internal fun tyres(season: Int) = activityProvider.launch {
        val activity = it as? AppCompatActivity ?: return@launch
        TyreBottomSheetFragment.instance(season).show(activity.supportFragmentManager, "TYRES")
    }

    fun featureNotificationOnboarding() = activityProvider.launch {
        val activity = it as? AppCompatActivity ?: return@launch
        NotificationOnboardingBottomSheetFragment.instance().show(activity.supportFragmentManager, "FEATURE_NOTIFICATIONS")
    }
}