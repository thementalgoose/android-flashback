package tmg.flashback.ui.dashboard.expanded

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import tmg.flashback.stats.ui.dashboard.constructors.ConstructorStandingsScreenVM
import tmg.flashback.stats.ui.dashboard.drivers.DriverStandingsScreenVM
import tmg.flashback.stats.ui.dashboard.schedule.ScheduleScreenVM
import tmg.flashback.style.text.TextHeadline1
import tmg.flashback.ui.components.navigation.NavigationColumn
import tmg.flashback.ui.components.navigation.NavigationTimelineItem
import tmg.flashback.ui.components.navigation.PipeType
import tmg.flashback.ui.dashboard.MenuSeasonItem
import tmg.flashback.ui.dashboard.compact.DashboardNavItem
import tmg.utilities.extensions.toEnum

private val contentWidth = 420.dp

@Composable
fun DashboardExpandedScreenVM(
    screenState: DashboardExpandedScreenState? = null,
    mainContent: @Composable (BoxScope.() -> Unit)? = null,
    subContent: @Composable (BoxScope.() -> Unit)? = null,
) {
    val viewModel = hiltViewModel<DashboardExpandedViewModel>()

    val tabState = viewModel.outputs.currentTab.observeAsState(viewModel.outputs.initialTab).value

    DashboardExpandedScreen(
        tabState = screenState ?: tabState,
        seasons = when (screenState == null) {
            true -> viewModel.outputs.seasons.observeAsState(initial = emptyList()).value
            false -> emptyList()
        },
        clickNavItem = viewModel.inputs::clickNavItem,
        clickSeasonItem = viewModel.inputs::clickSeason,
        mainContent = {
            when {
                mainContent != null -> mainContent.invoke(this)
                tabState.tab == DashboardExpandedNavItem.CALENDAR -> {
                    ScheduleScreenVM(
                        showMenu = false,
                        season = tabState.season
                    )
                }
                tabState.tab == DashboardExpandedNavItem.DRIVERS -> {
                    DriverStandingsScreenVM(
                        showMenu = false,
                        season = tabState.season
                    )
                }
                tabState.tab == DashboardExpandedNavItem.CONSTRUCTORS -> {
                    ConstructorStandingsScreenVM(
                        showMenu = false,
                        season = tabState.season
                    )
                }
            }
        },
        subContent = {
            when {
                subContent != null -> subContent.invoke(this)
            }
        }
    )
}

@Composable
fun DashboardExpandedScreen(
    tabState: DashboardExpandedScreenState,
    seasons: List<MenuSeasonItem>,
    clickNavItem: (DashboardExpandedNavItem) -> Unit,
    clickSeasonItem: (Int) -> Unit,
    mainContent: @Composable BoxScope.() -> Unit,
    subContent: @Composable BoxScope.() -> Unit
) {
    Row(Modifier.fillMaxSize()) {
        NavigationColumn(
            list = tabState.tab.toNavigationItems(),
            itemClicked = {
                val tab = it.id.toEnum() ?: DashboardExpandedNavItem.CALENDAR
                clickNavItem(tab)
            },
            timelineList = seasons.map {
                NavigationTimelineItem(
                    id = it.season.toString(),
                    pipeType = when {
                        it.isFirst -> PipeType.START
                        it.isLast -> PipeType.END
                        else -> PipeType.START_END
                    },
                    label = it.season.toString(),
                    color = it.colour,
                    isSelected = it.isSelected,
                )
            },
            timelineItemClicked = {
                clickSeasonItem(it.label.toInt())
            }
        )

        Box(
            modifier = Modifier
                .width(contentWidth)
                .fillMaxHeight(),
            content = mainContent
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            content = subContent
        )
    }
}