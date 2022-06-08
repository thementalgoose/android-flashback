package tmg.flashback.ui.dashboard

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import kotlinx.coroutines.launch
import org.koin.androidx.compose.viewModel
import tmg.flashback.R
import tmg.flashback.statistics.ui.search.SearchActivity
import tmg.flashback.stats.ui.dashboard.calendar.CalendarScreenVM
import tmg.flashback.stats.ui.dashboard.constructors.ConstructorStandingsScreenVM
import tmg.flashback.stats.ui.dashboard.drivers.DriverStandingsScreenVM
import tmg.flashback.style.AppTheme
import tmg.flashback.style.utils.WindowSize
import tmg.flashback.ui.components.layouts.OverlappingPanels
import tmg.flashback.ui.components.layouts.OverlappingPanelsValue
import tmg.flashback.ui.components.layouts.rememberOverlappingPanelsState
import tmg.flashback.ui.components.navigation.NavigationBar
import tmg.flashback.ui.components.navigation.NavigationItem
import tmg.flashback.ui.components.navigation.appBarHeight
import tmg.flashback.ui.dashboard.menu.MenuScreenVM
import tmg.utilities.extensions.toEnum

data class DashboardScreenState(
    val tab: DashboardNavItem,
    val season: Int
)

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DashboardScreen(
    windowSize: WindowSize
) {
    val viewModel by viewModel<DashboardViewModel>()

    val tabState = viewModel.outputs.currentTab.observeAsState()
    val panelsState = rememberOverlappingPanelsState(OverlappingPanelsValue.Closed)
    Scaffold(
        bottomBar = {
            val position = animateDpAsState(targetValue = if (panelsState.isStartPanelOpen) appBarHeight else 0.dp)
            NavigationBar(
                modifier = Modifier.offset(y = position.value),
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
                    val coroutineScope = rememberCoroutineScope()

                    // Background box
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(AppTheme.colors.backgroundPrimary)
                    )

                    MenuScreenVM(seasonClicked = {
                        viewModel.inputs.clickSeason(it)
                        coroutineScope.launch {
                            panelsState.closePanels()
                        }
                    })
                },
                panelCenter = {
                    val coroutineScope = rememberCoroutineScope()
                    val value = tabState.value

                    // Background box
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(AppTheme.colors.backgroundPrimary)
                    )

                    // Tabbed content
                    AnimatedVisibility(
                        visible = value?.tab == DashboardNavItem.CALENDAR,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        CalendarScreenVM(
                            showMenu = true,
                            season = value!!.season,
                            menuClicked = {
                                coroutineScope.launch {
                                    panelsState.openStartPanel()
                                }
                            }
                        )
                    }
                    AnimatedVisibility(
                        visible = value?.tab == DashboardNavItem.DRIVERS,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        DriverStandingsScreenVM(
                            showMenu = true,
                            season = value!!.season,
                            menuClicked = {
                                coroutineScope.launch {
                                    panelsState.openStartPanel()
                                }
                            }
                        )
                    }
                    AnimatedVisibility(
                        visible = value?.tab == DashboardNavItem.CONSTRUCTORS,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        ConstructorStandingsScreenVM(
                            showMenu = true,
                            season = value!!.season,
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