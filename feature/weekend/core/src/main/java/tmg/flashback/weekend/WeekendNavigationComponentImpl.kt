package tmg.flashback.weekend

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import tmg.flashback.weekend.contract.WeekendNavigationComponent
import tmg.flashback.weekend.contract.model.ScreenWeekendData
import tmg.flashback.weekend.presentation.WeekendScreen
import javax.inject.Inject

class WeekendNavigationComponentImpl @Inject constructor(): WeekendNavigationComponent {
    @Composable
    override fun Weekend(
        paddingValues: PaddingValues,
        actionUpClicked: () -> Unit,
        windowSizeClass: WindowSizeClass,
        weekendData: ScreenWeekendData
    ) {
        WeekendScreen(
            paddingValues = paddingValues,
            actionUpClicked = actionUpClicked,
            windowSizeClass = windowSizeClass,
            weekendInfo = weekendData,
        )
    }
}