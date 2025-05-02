package tmg.flashback.drivers.contract

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable

interface DriverNavigationComponent {

    @Composable
    fun DriverScreen(
        paddingValues: PaddingValues,
        actionUpClicked: () -> Unit,
        windowSizeClass: WindowSizeClass,
        driverId: String,
        driverName: String
    )

    @Composable
    fun DriverSeasonScreen(
        paddingValues: PaddingValues,
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

    @Composable
    fun DriverComparison(
        paddingValues: PaddingValues,
        actionUpClicked: () -> Unit,
        windowSizeClass: WindowSizeClass,
        season: Int
    )
}