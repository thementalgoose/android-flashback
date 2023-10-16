package tmg.flashback.weekend.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import tmg.flashback.googleanalytics.constants.AnalyticsConstants
import tmg.flashback.googleanalytics.presentation.ScreenView
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.components.navigation.NavigationBar
import tmg.flashback.weekend.contract.model.ScreenWeekendData
import tmg.flashback.weekend.ui.details.DetailsViewModel
import tmg.flashback.weekend.ui.qualifying.QualifyingHeader
import tmg.flashback.weekend.ui.qualifying.QualifyingViewModel
import tmg.flashback.weekend.ui.race.RaceResultType
import tmg.flashback.weekend.ui.race.RaceViewModel
import tmg.flashback.weekend.ui.sprint.SprintResultType
import tmg.flashback.weekend.ui.sprint.SprintViewModel
import tmg.flashback.weekend.ui.sprintquali.SprintQualifyingViewModel
import tmg.utilities.extensions.toEnum

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun WeekendEmbeddedScreenVM(
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    weekendInfo: ScreenWeekendData,
    viewModel: WeekendViewModel = hiltViewModel()
) {
    ScreenView(
        screenName = "Weekend", args = mapOf(
            AnalyticsConstants.analyticsSeason to weekendInfo.season.toString(),
            AnalyticsConstants.analyticsRound to weekendInfo.round.toString()
        )
    )

    val dbWeekendInfo = viewModel.outputs.weekendInfo.collectAsState(weekendInfo)
    val tabState = viewModel.outputs.tabs.collectAsState(
        listOf(
            WeekendScreenState(tab = WeekendNavItem.SCHEDULE, isSelected = true),
            WeekendScreenState(tab = WeekendNavItem.QUALIFYING, isSelected = false),
            WeekendScreenState(tab = WeekendNavItem.RACE, isSelected = false),
        )
    )

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val scrollToTop: () -> Unit = {
        coroutineScope.launch {
            listState.animateScrollToItem(0, 0)
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                list = tabState.value.toNavigationItems(),
                itemClicked = {
                    val tab = it.id.toEnum() ?: WeekendNavItem.SCHEDULE
                    viewModel.inputs.clickTab(tab)
                    scrollToTop()
                }
            )
        },
        content = {
            // Background box
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AppTheme.colors.backgroundPrimary)
            )
            val isRefreshing = viewModel.outputs.isRefreshing.collectAsState(false)

            val detailsVM: DetailsViewModel = hiltViewModel()
            val detailsList = detailsVM.outputs.list.collectAsState(emptyList())
            val qualifyingVM: QualifyingViewModel = hiltViewModel()
            val qualifyingList = qualifyingVM.outputs.list.collectAsState(emptyList())
            val qualifyingHeader = qualifyingVM.outputs.headersToShow.collectAsState(QualifyingHeader(true, true, true))
            val sprintQualifyingVM: SprintQualifyingViewModel = hiltViewModel()
            val sprintQualifyingList = sprintQualifyingVM.outputs.list.collectAsState(emptyList())
            val sprintVM: SprintViewModel = hiltViewModel()
            val sprintList = sprintVM.outputs.list.collectAsState(emptyList())
            val sprintResultType = sprintVM.outputs.sprintResultType.collectAsState(SprintResultType.DRIVERS)
            val raceVM: RaceViewModel = hiltViewModel()
            val raceList = raceVM.outputs.list.collectAsState(emptyList())
            val raceResultType = raceVM.outputs.raceResultType.collectAsState(RaceResultType.DRIVERS)

            Row(Modifier.fillMaxWidth()) {

            }
        }
    )
}