package tmg.flashback.weekend

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import tmg.flashback.weekend.contract.WeekendNavigationComponent
import tmg.flashback.weekend.contract.model.ScreenWeekendData
import tmg.flashback.weekend.ui.WeekendScreen
import javax.inject.Inject

class WeekendNavigationComponentImpl @Inject constructor(): WeekendNavigationComponent {
    @Composable
    override fun Weekend(
        actionUpClicked: () -> Unit,
        windowSizeClass: WindowSizeClass,
        weekendData: ScreenWeekendData
    ) {
        WeekendScreen(
            actionUpClicked = actionUpClicked,
            windowSizeClass = windowSizeClass,
            weekendInfo = weekendData,
        )
    }
}