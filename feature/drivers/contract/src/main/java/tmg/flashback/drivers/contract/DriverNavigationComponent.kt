package tmg.flashback.drivers.contract

import kotlinx.serialization.json.Json
import tmg.flashback.drivers.contract.model.DriverStatHistoryType
import tmg.flashback.drivers.contract.model.ScreenDriverData
import tmg.flashback.drivers.contract.model.ScreenDriverSeasonData
import tmg.flashback.navigation.NavigationDestination
import tmg.flashback.navigation.Screen


@JvmInline
value class ScreenDriver(val route: String)
val Screen.Driver get() = ScreenDriver("drivers/{data}")
fun ScreenDriver.with(
    driverId: String,
    driverName: String
) = NavigationDestination(
    this@with.route.replace("{data}", Json.encodeToString(ScreenDriverData.serializer(), ScreenDriverData(driverId, driverName)))
)

@JvmInline
value class ScreenDriverSeason(val route: String)
val Screen.DriverSeason get() = ScreenDriverSeason("drivers-season/{data}")
fun ScreenDriverSeason.with(
    driverId: String,
    driverName: String,
    season: Int
) = NavigationDestination(
    this@with.route.replace("{data}", Json.encodeToString(ScreenDriverSeasonData.serializer(), ScreenDriverSeasonData(driverId, driverName, season)))
)

interface DriverNavigationComponent {
    fun driverStatHistory(
        driverId: String,
        driverName: String,
        driverStatHistoryType: DriverStatHistoryType
    )
}