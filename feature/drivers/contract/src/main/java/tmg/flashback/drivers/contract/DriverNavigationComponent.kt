package tmg.flashback.drivers.contract

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import kotlinx.serialization.json.Json
import tmg.flashback.drivers.contract.model.DriverStatHistoryType
import tmg.flashback.drivers.contract.model.ScreenDriverData
import tmg.flashback.navigation.NavigationDestination
import tmg.flashback.navigation.Screen

@JvmInline
value class ScreenDriver(val route: String)
val Screen.Driver get() = ScreenDriver("driver/{data}")
fun ScreenDriver.with(
    driverId: String,
    driverName: String
) = NavigationDestination(
    this@with.route.replace("{data}", Json.encodeToString(ScreenDriverData.serializer(), ScreenDriverData(driverId, driverName)))
)

interface DriverNavigationComponent {

    @Composable
    fun DriverScreen(
        actionUpClicked: () -> Unit,
        windowSizeClass: WindowSizeClass,
        driverId: String,
        driverName: String
    )

    @Composable
    fun DriverSeasonScreen(
        actionUpClicked: () -> Unit,
        windowSizeClass: WindowSizeClass,
        driverId: String,
        driverName: String,
        season: Int,
        seasonClicked: (
            season: Int,
            round: Int,
            raceName: String,
            circuitId: String,
            circuitName: String,
            country: String,
            countryISO: String,
            dateString: String
        ) -> Unit,
    )

    fun driverStatHistory(
        driverId: String,
        driverName: String,
        driverStatHistoryType: DriverStatHistoryType
    )
}