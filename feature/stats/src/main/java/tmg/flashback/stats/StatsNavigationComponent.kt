package tmg.flashback.stats

import androidx.appcompat.app.AppCompatActivity
import tmg.flashback.stats.ui.drivers.stathistory.DriverStatHistoryBottomSheetFragment
import tmg.flashback.stats.ui.drivers.stathistory.DriverStatHistoryType
import tmg.flashback.stats.ui.events.EventsBottomSheetFragment
import tmg.flashback.stats.ui.feature.notificationonboarding.NotificationOnboardingBottomSheetFragment
import tmg.flashback.stats.ui.settings.notifications.reminder.UpNextReminderBottomSheetFragment
import tmg.flashback.stats.ui.tyres.TyreBottomSheetFragment
import tmg.flashback.stats.ui.weekend.WeekendInfo
import tmg.flashback.ui.navigation.ActivityProvider
import tmg.flashback.ui.navigation.NavigationDestination
import tmg.flashback.ui.navigation.Screen
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


@JvmInline
value class ScreenDriver(val route: String)
val Screen.Driver get() = ScreenDriver("drivers/{driverId}?driverName={driverName}")
fun ScreenDriver.with(
    driverId: String,
    driverName: String
) = NavigationDestination(
    this@with.route
        .replace("{driverId}", driverId)
        .replace("{driverName}", driverName)
)

@JvmInline
value class ScreenDriverSeason(val route: String)
val Screen.DriverSeason get() = ScreenDriverSeason("drivers/{driverId}/{season}?driverName={driverName}")
fun ScreenDriverSeason.with(
    driverId: String,
    driverName: String,
    season: Int
) = NavigationDestination(
    this@with.route
        .replace("{driverId}", driverId)
        .replace("{driverName}", driverName)
        .replace("{season}", season.toString())
)

@JvmInline
value class ScreenConstructor(val route: String)
val Screen.Constructor get() = ScreenConstructor("constructors/{constructorId}?constructorName={constructorName}")
fun ScreenConstructor.with(
    constructorId: String,
    constructorName: String
) = NavigationDestination(
    this@with.route
        .replace("{constructorId}", constructorId)
        .replace("{constructorName}", constructorName)
)

@JvmInline
value class ScreenConstructorSeason(val route: String)
val Screen.ConstructorSeason get() = ScreenConstructorSeason("constructors/{constructorId}/{season}?constructorName={constructorName}")
fun ScreenConstructorSeason.with(
    constructorId: String,
    constructorName: String,
    season: Int
) = NavigationDestination(
    this@with.route
        .replace("{constructorId}", constructorId)
        .replace("{constructorName}", constructorName)
        .replace("{season}", season.toString())
)


@JvmInline
value class ScreenWeekend(val route: String)
val Screen.Weekend get() = ScreenWeekend("weekend/{season}/{round}?" +
        "raceName={raceName}" + "&" +
        "circuitId={circuitId}" + "&" +
        "circuitName={circuitName}" + "&" +
        "country={country}" + "&" +
        "countryISO={countryISO}" + "&" +
        "date={date}"
)
fun ScreenWeekend.with(weekendInfo: WeekendInfo) = NavigationDestination(
    this@with.route
        .replace("{season}", weekendInfo.season.toString())
        .replace("{round}", weekendInfo.round.toString())
        .replace("{raceName}", weekendInfo.raceName)
        .replace("{circuitId}", weekendInfo.circuitId)
        .replace("{circuitName}", weekendInfo.circuitName)
        .replace("{country}", weekendInfo.country)
        .replace("{countryISO}", weekendInfo.countryISO)
        .replace("{date}", weekendInfo.dateString)
)

@JvmInline
value class ScreenCircuit(val route: String)
val Screen.Circuit get() = ScreenCircuit("circuit/{circuitId}?circuitName={circuitName}")
fun ScreenCircuit.with(
    circuitId: String,
    circuitName: String
) = NavigationDestination(
    this@with.route
        .replace("{circuitId}", circuitId)
        .replace("{circuitName}", circuitName)
)

val Screen.Search: NavigationDestination
    get() = NavigationDestination("search", launchSingleTop = true)

class StatsNavigationComponent @Inject constructor(
    private val activityProvider: ActivityProvider
) {
    fun upNext() = activityProvider.launch {
        val activity = it as? AppCompatActivity ?: return@launch
        UpNextReminderBottomSheetFragment().show(activity.supportFragmentManager, "UP_NEXT")
    }

    internal fun tyres(season: Int) = activityProvider.launch {
        val activity = it as? AppCompatActivity ?: return@launch
        TyreBottomSheetFragment.instance(season).show(activity.supportFragmentManager, "TYRES")
    }

    internal fun preseason(season: Int) = activityProvider.launch {
        val activity = it as? AppCompatActivity ?: return@launch
        EventsBottomSheetFragment.instance(season).show(activity.supportFragmentManager, "PRESEASON")
    }

    fun featureNotificationOnboarding() = activityProvider.launch {
        val activity = it as? AppCompatActivity ?: return@launch
        NotificationOnboardingBottomSheetFragment.instance().show(activity.supportFragmentManager, "FEATURE_NOTIFICATIONS")
    }

    fun driverStatHistory(
        driverId: String,
        driverName: String,
        driverStatHistoryType: DriverStatHistoryType
    ) = activityProvider.launch {
        val activity = it as? AppCompatActivity ?: return@launch
        DriverStatHistoryBottomSheetFragment.instance(driverId, driverName, driverStatHistoryType).show(activity.supportFragmentManager, "DRIVER_STAT_HISTORY")
    }
}