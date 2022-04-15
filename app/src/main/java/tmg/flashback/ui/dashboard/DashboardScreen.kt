package tmg.flashback.ui.dashboard

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.startActivity
import kotlinx.coroutines.launch
import org.koin.androidx.compose.viewModel
import tmg.flashback.R
import tmg.flashback.statistics.ui.search.SearchActivity
import tmg.flashback.stats.ui.dashboard.calendar.CalendarScreenVM
import tmg.flashback.stats.ui.dashboard.constructors.ConstructorStandingsScreenVM
import tmg.flashback.stats.ui.dashboard.drivers.DriverStandingsScreenVM
import tmg.flashback.style.utils.WindowSize
import tmg.flashback.ui.components.layouts.OverlappingPanels
import tmg.flashback.ui.components.layouts.OverlappingPanelsValue
import tmg.flashback.ui.components.layouts.rememberOverlappingPanelsState
import tmg.flashback.ui.components.navigation.NavigationBar
import tmg.flashback.ui.components.navigation.NavigationItem
import tmg.flashback.ui.dashboard.menu.MenuScreenVM
import tmg.utilities.extensions.toEnum

data class DashboardScreenState(
    val tab: DashboardNavItem,
    val season: Int
)

@Composable
fun DashboardScreenVM(
    windowSize: WindowSize
) {
    val viewModel by viewModel<DashboardViewModel>()

    val tabState = viewModel.outputs.currentTab.observeAsState()
    DashboardScreen(
        windowSize = windowSize
    )
}

@Composable
fun DashboardScreen(
    windowSize: WindowSize
) {
    val viewModel by viewModel<DashboardViewModel>()

    val tabState = viewModel.outputs.currentTab.observeAsState()
    val panelsState = rememberOverlappingPanelsState(OverlappingPanelsValue.Closed)
    Scaffold(
        bottomBar = {
            NavigationBar(
                list = tabState.value?.tab?.toNavigationItems() ?: emptyList(),
                itemClicked = {
                    val tab = it.id.toEnum() ?: DashboardNavItem.CALENDAR
                    viewModel.inputs.clickTab(tab)
                }
            )
        },
        content = {
            OverlappingPanels(
                panelsState = panelsState,
                panelStart = {
                    MenuScreenVM(
                        seasonClicked = viewModel.inputs::clickSeason
                    )
                },
                panelCenter = {
                    val coroutineScope = rememberCoroutineScope()
                    val value = tabState.value
                    when (value?.tab) {
                        DashboardNavItem.CALENDAR -> {
                            CalendarScreenVM(
                                showMenu = true,
                                season = value.season,
                                menuClicked = {
                                    coroutineScope.launch {
                                        panelsState.openStartPanel()
                                    }
                                }
                            )
                        }
                        DashboardNavItem.DRIVERS -> {
                            DriverStandingsScreenVM(
                                showMenu = true,
                                season = value.season,
                                menuClicked = {
                                    coroutineScope.launch {
                                        panelsState.openStartPanel()
                                    }
                                }
                            )
                        }
                        DashboardNavItem.CONSTRUCTORS -> {
                            ConstructorStandingsScreenVM(
                                showMenu = true,
                                season = value.season,
                                menuClicked = {
                                    coroutineScope.launch {
                                        panelsState.openStartPanel()
                                    }
                                }
                            )
                        }
                        null -> {
                            Text("HEY")
                        }
                    }
                }
            )
        }
    )
}

private fun DashboardNavItem.toNavigationItems(): List<NavigationItem> {
    return DashboardNavItem.values()
        .map {
            NavigationItem(
                id = it.name,
                label = it.label,
                icon = it.icon,
                isSelected = it == this
            )
        }
}