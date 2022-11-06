package tmg.flashback.ui.dashboard.compact

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import tmg.flashback.stats.ui.dashboard.constructors.ConstructorStandingsScreenVM
import tmg.flashback.stats.ui.dashboard.drivers.DriverStandingsScreenVM
import tmg.flashback.stats.ui.dashboard.schedule.ScheduleScreenVM
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.components.layouts.OverlappingPanels
import tmg.flashback.ui.components.layouts.OverlappingPanelsValue
import tmg.flashback.ui.components.layouts.rememberOverlappingPanelsState
import tmg.flashback.ui.components.navigation.NavigationBar
import tmg.flashback.ui.components.navigation.appBarHeight
import tmg.flashback.ui.dashboard.compact.menu.MenuScreenVM
import tmg.utilities.extensions.toEnum

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DashboardScreenVM() {
    val viewModel = hiltViewModel<DashboardViewModel>()

    DashboardScreen(
        tabState = viewModel.outputs.currentTab.observeAsState(viewModel.outputs.initialTab).value,
        clickTab = viewModel.inputs::clickTab,
        clickSeason = viewModel.inputs::clickSeason
    )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DashboardScreen(
    tabState: DashboardScreenState,
    clickTab: (DashboardNavItem) -> Unit,
    clickSeason: (Int) -> Unit,
) {
    val panelsState = rememberOverlappingPanelsState(OverlappingPanelsValue.Closed)

    Scaffold(
        bottomBar = {
            val position = animateDpAsState(targetValue = if (panelsState.isStartPanelOpen) appBarHeight else 0.dp)
            NavigationBar(
                modifier = Modifier.offset(y = position.value),
                list = tabState.tab.toNavigationItems(),
                itemClicked = {
                    val tab = it.id.toEnum() ?: DashboardNavItem.CALENDAR
                    clickTab(tab)
                }
            )
        },
        content = {
            OverlappingPanels(
                panelsState = panelsState,
                panelStart = {
                    val coroutineScope = rememberCoroutineScope()

                    // Background box
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(AppTheme.colors.backgroundPrimary)
                    )

                    MenuScreenVM(seasonClicked = {
                        clickSeason(it)
                        coroutineScope.launch {
                            panelsState.closePanels()
                        }
                    })
                },
                panelCenter = {
                    val coroutineScope = rememberCoroutineScope()
                    val value = tabState

                    // Background box
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(AppTheme.colors.backgroundPrimary)
                    )

                    // Tabbed content
                    AnimatedVisibility(
                        visible = value.tab == DashboardNavItem.CALENDAR,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        ScheduleScreenVM(
                            showMenu = true,
                            season = value.season,
                            menuClicked = {
                                coroutineScope.launch {
                                    panelsState.openStartPanel()
                                }
                            }
                        )
                    }
                    AnimatedVisibility(
                        visible = value.tab == DashboardNavItem.DRIVERS,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
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
                    AnimatedVisibility(
                        visible = value.tab == DashboardNavItem.CONSTRUCTORS,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
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
                }
            )
        }
    )
}