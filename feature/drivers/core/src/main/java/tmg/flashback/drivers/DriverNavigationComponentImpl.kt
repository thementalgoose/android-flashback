package tmg.flashback.drivers

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import tmg.flashback.crashlytics.manager.CrashlyticsManager
import tmg.flashback.drivers.contract.DriverNavigationComponent
import tmg.flashback.drivers.contract.model.DriverStatHistoryType
import tmg.flashback.drivers.presentation.season.DriverSeasonScreenVM
import tmg.flashback.drivers.presentation.stathistory.DriverStatHistoryBottomSheetFragment
import tmg.flashback.drivers.presentation.stathistory.analyticsKey
import tmg.flashback.navigation.ActivityProvider
import javax.inject.Inject

class DriverNavigationComponentImpl @Inject constructor(
    private val activityProvider: ActivityProvider,
    private val crashlyticsManager: CrashlyticsManager
): DriverNavigationComponent {

    @Composable
    override fun DriverSeasonScreen(
        actionUpClicked: () -> Unit,
        windowSizeClass: WindowSizeClass,
        driverId: String,
        driverName: String,
        season: Int,
    ) {
        DriverSeasonScreenVM(
            actionUpClicked = actionUpClicked,
            windowSizeClass = windowSizeClass,
            driverId = driverId,
            driverName = driverName,
            season = season,
        )
    }

    override fun driverStatHistory(
        driverId: String,
        driverName: String,
        driverStatHistoryType: DriverStatHistoryType
    ) = activityProvider.launch {
        crashlyticsManager.log("Navigating to driver stat history $driverId ${driverStatHistoryType.analyticsKey}")
        val activity = it as? AppCompatActivity ?: return@launch
        DriverStatHistoryBottomSheetFragment.instance(driverId, driverName, driverStatHistoryType).show(activity.supportFragmentManager, "DRIVER_STAT_HISTORY")
    }
}