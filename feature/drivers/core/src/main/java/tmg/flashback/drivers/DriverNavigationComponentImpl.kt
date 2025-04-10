package tmg.flashback.drivers

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import tmg.flashback.drivers.contract.DriverNavigationComponent
import tmg.flashback.drivers.presentation.comparison.DriverComparisonScreenVM
import tmg.flashback.drivers.presentation.overview.DriverOverviewScreenVM
import tmg.flashback.drivers.presentation.season.DriverSeasonScreenVM
import javax.inject.Inject

class DriverNavigationComponentImpl @Inject constructor(): DriverNavigationComponent {

    @Composable
    override fun DriverSeasonScreen(
        paddingValues: PaddingValues,
        actionUpClicked: () -> Unit,
        windowSizeClass: WindowSizeClass,
        driverId: String,
        driverName: String,
        season: Int,
        seasonClicked: (season: Int, round: Int, raceName: String, circuitId: String, circuitName: String, country: String, countryISO: String, dateString: String) -> Unit
    ) {
        DriverSeasonScreenVM(
            paddingValues = paddingValues,
            actionUpClicked = actionUpClicked,
            windowSizeClass = windowSizeClass,
            driverId = driverId,
            driverName = driverName,
            season = season,
        )
    }

    @Composable
    override fun DriverComparison(
        paddingValues: PaddingValues,
        actionUpClicked: () -> Unit,
        windowSizeClass: WindowSizeClass,
        season: Int
    ) {
        DriverComparisonScreenVM(
            paddingValues = paddingValues,
            actionUpClicked = actionUpClicked,
            windowSizeClass = windowSizeClass,
            season = season
        )
    }

    @Composable
    override fun DriverScreen(
        paddingValues: PaddingValues,
        actionUpClicked: () -> Unit,
        windowSizeClass: WindowSizeClass,
        driverId: String,
        driverName: String
    ) {
        DriverOverviewScreenVM(
            paddingValues = paddingValues,
            actionUpClicked = actionUpClicked,
            windowSizeClass = windowSizeClass,
            driverId = driverId,
            driverName = driverName
        )
    }
}