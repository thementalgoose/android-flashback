package tmg.flashback.weekend.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.weekend.contract.model.ScreenWeekendData

@Composable
fun WeekendScreen(
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    weekendInfo: ScreenWeekendData,
    viewModel: WeekendViewModel = hiltViewModel()
) {
    viewModel.load(weekendInfo.season, weekendInfo.round)
    if (windowSizeClass.widthSizeClass != WindowWidthSizeClass.Expanded) {
        WeekendTabScreen(
            actionUpClicked = actionUpClicked,
            windowSizeClass = windowSizeClass,
            weekendInfo = weekendInfo,
            viewModel = viewModel
        )
    } else {
        WeekendListScreen(
            actionUpClicked = actionUpClicked,
            windowSizeClass = windowSizeClass,
            weekendInfo = weekendInfo,
            viewModel = viewModel
        )
    }
}