package tmg.flashback.stats

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.*
import androidx.navigation.compose.composable
import org.threeten.bp.LocalDate
import tmg.flashback.stats.ui.circuits.CircuitScreenVM
import tmg.flashback.stats.ui.constructors.overview.ConstructorOverviewScreenVM
import tmg.flashback.stats.ui.constructors.season.ConstructorSeasonScreenVM
import tmg.flashback.stats.ui.drivers.overview.DriverOverviewScreenVM
import tmg.flashback.stats.ui.drivers.season.DriverSeasonScreenVM
import tmg.flashback.stats.ui.feature.notificationonboarding.NotificationOnboardingBottomSheetFragment
import tmg.flashback.stats.ui.search.SearchScreenVM
import tmg.flashback.stats.ui.settings.notifications.reminder.UpNextReminderBottomSheetFragment
import tmg.flashback.stats.ui.tyres.TyreBottomSheetFragment
import tmg.flashback.stats.ui.weekend.WeekendInfo
import tmg.flashback.stats.ui.weekend.WeekendScreenVM
import tmg.flashback.ui.navigation.*
import tmg.flashback.ui.navigation.Navigator
import tmg.utilities.extensions.format
import tmg.utilities.extensions.toLocalDate

val Screen.DriverPlaceholder: String get() = "drivers/{driverId}?driverName={driverName}"
fun Screen.Driver(driverId: String, driverName: String): NavigationDestination = object : NavigationDestination {
    override val route: String = "drivers/$driverId?driverName=$driverName"
}

val Screen.DriverSeasonPlaceholder: String get() = "drivers/{driverId}/{season}?driverName={driverName}"
fun Screen.Driver(driverId: String, driverName: String, season: Int): NavigationDestination = object : NavigationDestination {
    override val route: String = "drivers/$driverId/$season?driverName=$driverName"
}

val Screen.ConstructorPlaceholder: String get() = "constructors/{constructorId}?constructorName={constructorName}"
fun Screen.Constructor(constructorId: String, constructorName: String): NavigationDestination = object : NavigationDestination {
    override val route: String = "constructors/$constructorId?constructorName=$constructorName"
}


//val raceName: String,
//val circuitId: String,
//val circuitName: String,
//val country: String,
//val countryISO: String,
//val date: LocalDate
val Screen.WeekendPlaceholder: String get() = "weekend/{season}/{round}?" +
        "raceName={raceName}" + "&" +
        "circuitId={circuitId}" + "&" +
        "circuitName={circuitName}" + "&" +
        "country={country}" + "&" +
        "countryISO={countryISO}" + "&" +
        "date={date}"
fun Screen.Weekend(weekendInfo: WeekendInfo): NavigationDestination = object : NavigationDestination {
    override val route: String = "weekend/${weekendInfo.season}/${weekendInfo.round}?" +
            "raceName=${weekendInfo.raceName}" + "&" +
            "circuitId=${weekendInfo.circuitId}" + "&" +
            "circuitName=${weekendInfo.circuitName}" + "&" +
            "country=${weekendInfo.country}" + "&" +
            "countryISO=${weekendInfo.countryISO}" + "&" +
            "date=${weekendInfo.date.format("yyyy-MM-dd")}"
}

val Screen.CircuitPlaceholder: String get() = "circuit/{circuitId}?circuitName={circuitName}"
fun Screen.Circuit(circuitId: String, circuitName: String): NavigationDestination = object : NavigationDestination {
    override val route: String = "circuit/$circuitId?circuitName=$circuitName"
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

fun NavGraphBuilder.stats(navController: NavController) {
    composable(Screen.WeekendPlaceholder, arguments = listOf(
        navIntRequired("season"),
        navIntRequired("round")
    )) {
        val season = it.arguments?.getInt("season")!!
        val round = it.arguments?.getInt("round")!!
        WeekendScreenVM(
            weekendInfo = WeekendInfo(
                season = season,
                round = round,
                raceName = it.arguments?.getString("raceName") ?: "",
                circuitId = it.arguments?.getString("circuitId") ?: "",
                circuitName = it.arguments?.getString("circuitName") ?: "",
                country = it.arguments?.getString("country") ?: "",
                countryISO = it.arguments?.getString("countryISO") ?: "",
                date = it.arguments?.getString("date")?.toLocalDate("yyyy-MM-dd") ?: LocalDate.now(),
            ),
            actionUpClicked = { navController.popBackStack() }
        )
    }

    composable(Screen.CircuitPlaceholder, arguments = listOf(
        navStringRequired("circuitId")
    )) {
        val circuitId = it.arguments?.getString("circuitId")!!
        CircuitScreenVM(
            circuitId = circuitId,
            circuitName = it.arguments?.getString("circuitName") ?: "",
            actionUpClicked = { navController.popBackStack() }
        )
    }

    composable(Screen.DriverPlaceholder, arguments = listOf(
        navStringRequired("driverId")
    )) {
        val driverId = it.arguments?.getString("driverId")!!
        DriverOverviewScreenVM(
            driverId = driverId,
            driverName = it.arguments?.getString("driverName") ?: "",
            actionUpClicked = { navController.popBackStack() }
        )
    }

    composable(Screen.DriverSeasonPlaceholder, arguments = listOf(
        navStringRequired("driverId"),
        navIntRequired("season")
    )) {
        val driverId = it.arguments?.getString("driverId")!!
        val season = it.arguments?.getInt("season")!!
        DriverSeasonScreenVM(
            driverId = driverId,
            driverName = it.arguments?.getString("driverName") ?: "",
            season = season,
            actionUpClicked = { navController.popBackStack() }
        )
    }

    composable(Screen.ConstructorPlaceholder, arguments = listOf(
        navStringRequired("constructorId")
    )) {
        val constructorId = it.arguments?.getString("constructorId")!!
        ConstructorOverviewScreenVM(
            constructorId = constructorId,
            constructorName = it.arguments?.getString("constructorName") ?: "",
            actionUpClicked = { navController.popBackStack() }
        )
    }

    composable(Screen.Search.route) {
        SearchScreenVM(
            actionUpClicked = { navController.popBackStack() }
        )
    }
}

class StatsNavigationComponent(
    private val navigator: Navigator,
    private val activityProvider: ActivityProvider
) {
    fun driverOverview(id: String, name: String) {
        navigator.navigate(Screen.Driver(
            driverId = id,
            driverName = name
        ))
    }

    fun driverSeason(id: String, name: String, season: Int) {
        navigator.navigate(Screen.Driver(
            driverId = id,
            driverName = name,
            season = season
        ))
    }

    fun constructorOverview(id: String, name: String) {
        navigator.navigate(Screen.Constructor(
            constructorId = id,
            constructorName = name
        ))
    }

    @Deprecated("Not yet implemented!")
    fun constructorSeason(id: String, name: String, season: Int) {
        TODO()
    }

    fun weekend(weekendInfo: WeekendInfo) {
        navigator.navigate(Screen.Weekend(
            weekendInfo = weekendInfo
        ))
    }

    fun search() {
        navigator.navigate(Screen.Search)
    }

    fun circuit(circuitId: String, circuitName: String) {
        navigator.navigate(Screen.Circuit(
            circuitId = circuitId,
            circuitName = circuitName
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