package tmg.flashback.stats.ui.weekend

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import tmg.flashback.stats.analytics.AnalyticsConstants
import tmg.flashback.stats.analytics.AnalyticsConstants.analyticsRound
import tmg.flashback.stats.analytics.AnalyticsConstants.analyticsSeason
import tmg.flashback.stats.ui.weekend.constructor.ConstructorScreenVM
import tmg.flashback.stats.ui.weekend.constructor.ConstructorViewModel
import tmg.flashback.stats.ui.weekend.constructor.constructor
import tmg.flashback.stats.ui.weekend.qualifying.QualifyingScreenVM
import tmg.flashback.stats.ui.weekend.race.RaceScreenVM
import tmg.flashback.stats.ui.weekend.details.DetailsScreenVM
import tmg.flashback.stats.ui.weekend.details.DetailsViewModel
import tmg.flashback.stats.ui.weekend.details.details
import tmg.flashback.stats.ui.weekend.info.RaceInfoHeader
import tmg.flashback.stats.ui.weekend.qualifying.QualifyingHeader
import tmg.flashback.stats.ui.weekend.qualifying.QualifyingViewModel
import tmg.flashback.stats.ui.weekend.qualifying.qualifying
import tmg.flashback.stats.ui.weekend.race.RaceViewModel
import tmg.flashback.stats.ui.weekend.race.race
import tmg.flashback.stats.ui.weekend.sprint.SprintScreenVM
import tmg.flashback.stats.ui.weekend.sprint.SprintViewModel
import tmg.flashback.stats.ui.weekend.sprint.sprint
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.components.analytics.ScreenView
import tmg.flashback.ui.components.loading.Fade
import tmg.flashback.ui.components.navigation.NavigationBar
import tmg.flashback.ui.components.navigation.NavigationItem
import tmg.flashback.ui.components.swiperefresh.SwipeRefresh
import tmg.utilities.extensions.toEnum

data class WeekendScreenState(
    val tab: WeekendNavItem,
    val isSelected: Boolean
)

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun WeekendScreenVM(
    weekendInfo: WeekendInfo,
    actionUpClicked: () -> Unit,
    viewModel: WeekendViewModel = hiltViewModel()
) {
    viewModel.inputs.load(
        season = weekendInfo.season,
        round = weekendInfo.round
    )

    ScreenView(screenName = "Weekend", args = mapOf(
        analyticsSeason to weekendInfo.season.toString(),
        analyticsRound to weekendInfo.round.toString()
    ))

    val dbWeekendInfo = viewModel.outputs.weekendInfo.observeAsState(weekendInfo)
    val tabState = viewModel.outputs.tabs.observeAsState(listOf(
        WeekendScreenState(tab = WeekendNavItem.SCHEDULE, isSelected = true),
        WeekendScreenState(tab = WeekendNavItem.QUALIFYING, isSelected = false),
        WeekendScreenState(tab = WeekendNavItem.RACE, isSelected = false),
        WeekendScreenState(tab = WeekendNavItem.CONSTRUCTOR, isSelected = false)
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
            val isRefreshing = viewModel.outputs.isRefreshing.observeAsState(false)

            val detailsVM: DetailsViewModel = hiltViewModel()
            val detailsList = detailsVM.outputs.list.observeAsState(emptyList())
            val qualifyingVM: QualifyingViewModel = hiltViewModel()
            val qualifyingList = qualifyingVM.outputs.list.observeAsState(emptyList())
            val qualifyingHeader = qualifyingVM.outputs.headersToShow.observeAsState(QualifyingHeader(true, true, true))
            val sprintVM: SprintViewModel = hiltViewModel()
            val sprintList = sprintVM.outputs.list.observeAsState(emptyList())
            val raceVM: RaceViewModel = hiltViewModel()
            val raceList = raceVM.outputs.list.observeAsState(emptyList())
            val constructorVM: ConstructorViewModel = hiltViewModel()
            val constructorList = constructorVM.outputs.list.observeAsState(emptyList())


            SwipeRefresh(
                isLoading = isRefreshing.value,
                onRefresh = viewModel.inputs::refresh
            ) {
                LazyColumn(
                    state = listState,
                    content = {
                        item("header") {
                            RaceInfoHeader(
                                model = dbWeekendInfo.value,
                                actionUpClicked = actionUpClicked
                            )
                        }

                        detailsVM.inputs.load(weekendInfo.season, weekendInfo.round)
                        qualifyingVM.inputs.load(weekendInfo.season, weekendInfo.round)
                        sprintVM.inputs.load(weekendInfo.season, weekendInfo.round)
                        raceVM.inputs.load(weekendInfo.season, weekendInfo.round)
                        constructorVM.inputs.load(weekendInfo.season, weekendInfo.round)

                        when (tabState.value.first { it.isSelected }.tab) {
                            WeekendNavItem.SCHEDULE -> {
                                details(
                                    linkClicked = detailsVM.inputs::linkClicked,
                                    items = detailsList.value
                                )
                            }
                            WeekendNavItem.QUALIFYING -> {
                                qualifying(
                                    driverClicked = qualifyingVM.inputs::clickDriver,
                                    list = qualifyingList.value,
                                    header = qualifyingHeader.value
                                )
                            }
                            WeekendNavItem.SPRINT -> {
                                sprint(
                                    list = sprintList.value,
                                    driverClicked = sprintVM.inputs::clickDriver
                                )
                            }
                            WeekendNavItem.RACE -> {
                                race(
                                    list = raceList.value,
                                    driverClicked = raceVM.inputs::clickDriver
                                )
                            }
                            WeekendNavItem.CONSTRUCTOR -> {
                                constructor(
                                    list = constructorList.value,
                                    itemClicked = constructorVM.inputs::clickItem
                                )
                            }
                        }
                    }
                )
            }
        }
    )
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