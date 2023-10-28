package tmg.flashback.weekend.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import tmg.flashback.googleanalytics.constants.AnalyticsConstants.analyticsRound
import tmg.flashback.googleanalytics.constants.AnalyticsConstants.analyticsSeason
import tmg.flashback.style.AppTheme
import tmg.flashback.googleanalytics.presentation.ScreenView
import tmg.flashback.ui.components.navigation.NavigationBar
import tmg.flashback.ui.components.navigation.NavigationItem
import tmg.flashback.ui.components.navigation.appBarHeight
import tmg.flashback.ui.components.swiperefresh.SwipeRefresh
import tmg.flashback.weekend.contract.model.ScreenWeekendData
import tmg.flashback.weekend.presentation.details.DetailsViewModel
import tmg.flashback.weekend.presentation.details.details
import tmg.flashback.weekend.presentation.info.RaceInfoHeader
import tmg.flashback.weekend.presentation.qualifying.QualifyingHeader
import tmg.flashback.weekend.presentation.qualifying.QualifyingViewModel
import tmg.flashback.weekend.presentation.qualifying.qualifying
import tmg.flashback.weekend.presentation.race.RaceResultType
import tmg.flashback.weekend.presentation.race.RaceViewModel
import tmg.flashback.weekend.presentation.race.race
import tmg.flashback.weekend.presentation.sprint.SprintResultType
import tmg.flashback.weekend.presentation.sprint.SprintViewModel
import tmg.flashback.weekend.presentation.sprint.sprint
import tmg.flashback.weekend.presentation.sprintquali.SprintQualifyingViewModel
import tmg.flashback.weekend.presentation.sprintquali.sprintQualifying
import tmg.utilities.extensions.toEnum

data class WeekendScreenState(
    val tab: WeekendNavItem,
    val isSelected: Boolean
)

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun WeekendTabScreen(
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    weekendInfo: ScreenWeekendData,
    viewModel: WeekendViewModel
) {
    ScreenView(screenName = "Weekend", args = mapOf(
        analyticsSeason to weekendInfo.season.toString(),
        analyticsRound to weekendInfo.round.toString()
    ))

    val dbWeekendInfo = viewModel.outputs.weekendInfo.collectAsState(weekendInfo)
    val tabState = viewModel.outputs.tabs.collectAsState(listOf(
        WeekendScreenState(tab = WeekendNavItem.SCHEDULE, isSelected = true),
        WeekendScreenState(tab = WeekendNavItem.QUALIFYING, isSelected = false),
        WeekendScreenState(tab = WeekendNavItem.RACE, isSelected = false),
    ))

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
            Box(modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.colors.backgroundPrimary)
            )
            val isRefreshing = viewModel.outputs.isRefreshing.collectAsState(false)

            val detailsVM: DetailsViewModel = hiltViewModel()
            val detailsList = detailsVM.outputs.list.collectAsState(emptyList())
            val qualifyingVM: QualifyingViewModel = hiltViewModel()
            val qualifyingList = qualifyingVM.outputs.list.collectAsState(emptyList())
            val qualifyingHeader = qualifyingVM.outputs.headersToShow.collectAsState(
                QualifyingHeader(true, true, true)
            )
            val sprintQualifyingVM: SprintQualifyingViewModel = hiltViewModel()
            val sprintQualifyingList = sprintQualifyingVM.outputs.list.collectAsState(emptyList())
            val sprintVM: SprintViewModel = hiltViewModel()
            val sprintList = sprintVM.outputs.list.collectAsState(emptyList())
            val sprintResultType = sprintVM.outputs.sprintResultType.collectAsState(SprintResultType.DRIVERS)
            val raceVM: RaceViewModel = hiltViewModel()
            val raceList = raceVM.outputs.list.collectAsState(emptyList())
            val raceResultType = raceVM.outputs.raceResultType.collectAsState(RaceResultType.DRIVERS)

            detailsVM.inputs.load(weekendInfo.season, weekendInfo.round)
            qualifyingVM.inputs.load(weekendInfo.season, weekendInfo.round)
            sprintQualifyingVM.inputs.load(weekendInfo.season, weekendInfo.round)
            sprintVM.inputs.load(weekendInfo.season, weekendInfo.round)
            raceVM.inputs.load(weekendInfo.season, weekendInfo.round)

            SwipeRefresh(
                isLoading = isRefreshing.value,
                onRefresh = viewModel.inputs::refresh
            ) {
                LazyColumn(
                    state = listState,
                    content = {
                        item("header") {
                            RaceInfoHeader(
                                model = weekendInfo,
                                actionUpClicked = actionUpClicked,
                            )
                        }

                        when (tabState.value.firstOrNull { it.isSelected }?.tab) {
                            WeekendNavItem.SCHEDULE -> {
                                details(
                                    weekendInfo = weekendInfo,
                                    items = detailsList.value,
                                    linkClicked = detailsVM.inputs::linkClicked
                                )
                            }
                            WeekendNavItem.QUALIFYING -> {
                                qualifying(
                                    driverClicked = qualifyingVM.inputs::clickDriver,
                                    list = qualifyingList.value,
                                    header = qualifyingHeader.value
                                )
                            }
                            WeekendNavItem.SPRINT_QUALIFYING -> {
                                sprintQualifying(
                                    driverClicked = sprintQualifyingVM.inputs::clickDriver,
                                    list = sprintQualifyingList.value
                                )
                            }
                            WeekendNavItem.SPRINT -> {
                                sprint(
                                    season = weekendInfo.season,
                                    showSprintType = sprintVM.inputs::show,
                                    sprintResultType = sprintResultType.value,
                                    list = sprintList.value,
                                    driverClicked = sprintVM.inputs::clickDriver,
                                    constructorClicked = sprintVM.inputs::clickConstructor
                                )
                            }
                            WeekendNavItem.RACE -> {
                                race(
                                    season = weekendInfo.season,
                                    showRaceType = raceVM.inputs::show,
                                    raceResultType = raceResultType.value,
                                    list = raceList.value,
                                    driverClicked = raceVM.inputs::clickDriver,
                                    constructorClicked = raceVM.inputs::clickConstructor
                                )
                            }
                            null -> { }
                        }

                        item(key = "footer") {
                            Spacer(Modifier.height(appBarHeight))
                        }
                    }
                )
            }
        }
    )
}

internal fun List<WeekendScreenState>.toNavigationItems(): List<NavigationItem> {
    return this
        .map {
            NavigationItem(
                id = it.tab.name,
                label = it.tab.label,
                icon = it.tab.icon,
                isSelected = it.isSelected
            )
        }
}