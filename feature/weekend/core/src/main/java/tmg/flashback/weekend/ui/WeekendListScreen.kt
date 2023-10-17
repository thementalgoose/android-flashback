package tmg.flashback.weekend.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.googleanalytics.constants.AnalyticsConstants
import tmg.flashback.googleanalytics.presentation.ScreenView
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.components.navigation.appBarHeight
import tmg.flashback.ui.components.swiperefresh.SwipeRefresh
import tmg.flashback.weekend.R
import tmg.flashback.weekend.contract.model.ScreenWeekendData
import tmg.flashback.weekend.ui.details.DetailsViewModel
import tmg.flashback.weekend.ui.details.details
import tmg.flashback.weekend.ui.info.RaceInfoHeader
import tmg.flashback.weekend.ui.qualifying.QualifyingHeader
import tmg.flashback.weekend.ui.qualifying.QualifyingViewModel
import tmg.flashback.weekend.ui.qualifying.qualifying
import tmg.flashback.weekend.ui.race.RaceResultType
import tmg.flashback.weekend.ui.race.RaceViewModel
import tmg.flashback.weekend.ui.race.race
import tmg.flashback.weekend.ui.shared.CollapsibleBox
import tmg.flashback.weekend.ui.sprint.SprintResultType
import tmg.flashback.weekend.ui.sprint.SprintViewModel
import tmg.flashback.weekend.ui.sprint.sprint
import tmg.flashback.weekend.ui.sprintquali.SprintQualifyingViewModel
import tmg.flashback.weekend.ui.sprintquali.sprintQualifying

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun WeekendListScreen(
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    weekendInfo: ScreenWeekendData,
    viewModel: WeekendViewModel
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

    val qualifyingExpanded = remember { mutableStateOf(false) }
    val sprintQualifyingExpanded = remember { mutableStateOf(false) }
    val sprintRaceExpanded = remember { mutableStateOf(false) }
    val raceExpanded = remember { mutableStateOf(true) }

    val background = AppTheme.colors.backgroundSecondary

    detailsVM.inputs.load(weekendInfo.season, weekendInfo.round)
    qualifyingVM.inputs.load(weekendInfo.season, weekendInfo.round)
    sprintQualifyingVM.inputs.load(weekendInfo.season, weekendInfo.round)
    sprintVM.inputs.load(weekendInfo.season, weekendInfo.round)
    raceVM.inputs.load(weekendInfo.season, weekendInfo.round)

    SwipeRefresh(
        isLoading = isRefreshing.value,
        onRefresh = viewModel.inputs::refresh
    ) {
        LazyColumn(content = {
            item("header") {
                RaceInfoHeader(
                    model = dbWeekendInfo.value ?: weekendInfo,
                    showBack = false,
                    actionUpClicked = actionUpClicked,
                )
            }

            details(
                weekendInfo = dbWeekendInfo.value ?: weekendInfo,
                items = detailsList.value,
                showTrack = false,
                linkClicked = detailsVM.inputs::linkClicked
            )

            item("qualifying") {
                CollapsibleBox(
                    showCheckmark = qualifyingList.value.size > 1,
                    isExpanded = qualifyingExpanded,
                    title = stringResource(id = R.string.nav_qualifying)
                )
            }
            qualifying(
                itemModifier = Modifier
                    .padding(horizontal = AppTheme.dimens.medium)
                    .background(background),
                driverClicked = qualifyingVM.inputs::clickDriver,
                list = qualifyingList.value.takeIf { qualifyingExpanded.value } ?: emptyList(),
                header = qualifyingHeader.value
            )


            if (tabState.value.any { it.tab == WeekendNavItem.SPRINT_QUALIFYING }) {
                item("sprintQualifying") {
                    CollapsibleBox(
                        showCheckmark = sprintQualifyingList.value.size > 1,
                        isExpanded = sprintQualifyingExpanded,
                        title = stringResource(id = R.string.nav_sprint_qualifying)
                    )
                }
                sprintQualifying(
                    itemModifier = Modifier
                        .padding(horizontal = AppTheme.dimens.medium)
                        .background(background),
                    driverClicked = sprintQualifyingVM.inputs::clickDriver,
                    list = sprintQualifyingList.value.takeIf { sprintQualifyingExpanded.value } ?: emptyList()
                )
            }

            if (tabState.value.any { it.tab == WeekendNavItem.SPRINT }) {
                item("sprintRace") {
                    CollapsibleBox(
                        showCheckmark = sprintList.value.size > 1,
                        isExpanded = sprintRaceExpanded,
                        title = stringResource(id = R.string.nav_sprint)
                    )
                }
                sprint(
                    itemModifier = Modifier
                        .padding(horizontal = AppTheme.dimens.medium)
                        .background(background),
                    season = weekendInfo.season,
                    showSprintType = sprintVM.inputs::show,
                    sprintResultType = sprintResultType.value,
                    list = sprintList.value.takeIf { sprintRaceExpanded.value } ?: emptyList(),
                    driverClicked = sprintVM.inputs::clickDriver,
                    constructorClicked = sprintVM.inputs::clickConstructor
                )
            }

            item("race") {
                CollapsibleBox(
                    showCheckmark = raceList.value.size > 1,
                    isExpanded = raceExpanded,
                    title = stringResource(id = R.string.nav_race)
                )
            }
            race(
                itemModifier = Modifier
                    .padding(horizontal = AppTheme.dimens.medium)
                    .background(background),
                season = weekendInfo.season,
                showRaceType = raceVM.inputs::show,
                raceResultType = raceResultType.value,
                list = raceList.value.takeIf { raceExpanded.value } ?: emptyList(),
                driverClicked = raceVM.inputs::clickDriver,
                constructorClicked = raceVM.inputs::clickConstructor
            )

            item(key = "footer") {
                Spacer(Modifier.height(appBarHeight))
            }
        })
    }
}