package tmg.flashback.drivers.contract

import tmg.flashback.drivers.contract.model.DriverStatHistoryType
import tmg.flashback.navigation.ActivityProvider
import tmg.flashback.navigation.NavigationDestination
import tmg.flashback.navigation.Screen
import javax.inject.Inject


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

interface DriverNavigationComponent {
    fun driverStatHistory(
        driverId: String,
        driverName: String,
        driverStatHistoryType: DriverStatHistoryType
    )
}