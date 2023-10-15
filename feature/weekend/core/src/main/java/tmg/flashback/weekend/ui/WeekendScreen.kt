package tmg.flashback.weekend.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import tmg.flashback.googleanalytics.constants.AnalyticsConstants.analyticsRound
import tmg.flashback.googleanalytics.constants.AnalyticsConstants.analyticsSeason
import tmg.flashback.style.AppTheme
import tmg.flashback.googleanalytics.presentation.ScreenView
import tmg.flashback.ui.components.navigation.NavigationBar
import tmg.flashback.ui.components.navigation.NavigationItem
import tmg.flashback.ui.components.swiperefresh.SwipeRefresh
import tmg.flashback.weekend.contract.model.ScreenWeekendData
import tmg.flashback.weekend.ui.details.DetailsModel
import tmg.flashback.weekend.ui.details.DetailsViewModel
import tmg.flashback.weekend.ui.details.details
import tmg.flashback.weekend.ui.info.RaceInfoHeader
import tmg.flashback.weekend.ui.qualifying.QualifyingHeader
import tmg.flashback.weekend.ui.qualifying.QualifyingViewModel
import tmg.flashback.weekend.ui.qualifying.qualifying
import tmg.flashback.weekend.ui.race.RaceResultType
import tmg.flashback.weekend.ui.race.RaceViewModel
import tmg.flashback.weekend.ui.race.race
import tmg.flashback.weekend.ui.sprint.SprintResultType
import tmg.flashback.weekend.ui.sprint.SprintViewModel
import tmg.flashback.weekend.ui.sprint.sprint
import tmg.flashback.weekend.ui.sprintquali.SprintQualifyingViewModel
import tmg.flashback.weekend.ui.sprintquali.sprintQualifying
import tmg.utilities.extensions.toEnum

data class WeekendScreenState(
    val tab: WeekendNavItem,
    val isSelected: Boolean
)

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun WeekendScreenVM(
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    weekendInfo: ScreenWeekendData,
    viewModel: WeekendViewModel = hiltViewModel()
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

            SwipeRefresh(
                isLoading = isRefreshing.value,
                onRefresh = viewModel.inputs::refresh
            ) {
                LazyColumn(
                    state = listState,
                    content = {
                        item("header") {
                            RaceInfoHeader(
                                model = dbWeekendInfo.value ?: weekendInfo,
                                actionUpClicked = actionUpClicked,
                            )
                        }

                        detailsVM.inputs.load(weekendInfo.season, weekendInfo.round)
                        qualifyingVM.inputs.load(weekendInfo.season, weekendInfo.round)
                        sprintQualifyingVM.inputs.load(weekendInfo.season, weekendInfo.round)
                        sprintVM.inputs.load(weekendInfo.season, weekendInfo.round)
                        raceVM.inputs.load(weekendInfo.season, weekendInfo.round)

                        when (tabState.value.first { it.isSelected }.tab) {
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
                        }
                    }
                )
            }
        }
    )
}

@Composable
private fun Links(
    details: List<DetailsModel>,
    linkClicked: (DetailsModel.Link) -> Unit
) {
    val links = details.firstNotNullOfOrNull { it as? DetailsModel.Links }
    links?.links?.forEach { link ->
        IconButton(onClick = { linkClicked(link) }) {
            Icon(
                painter = painterResource(id = link.icon),
                contentDescription = stringResource(id = link.label),
                tint = AppTheme.colors.contentPrimary
            )
        }
    }
}

private fun List<WeekendScreenState>.toNavigationItems(): List<NavigationItem> {
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