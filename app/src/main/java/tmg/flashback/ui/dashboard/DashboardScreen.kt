@file:OptIn(ExperimentalMaterial3Api::class)

package tmg.flashback.ui.dashboard

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.components.layouts.OverlappingPanels
import tmg.flashback.ui.components.layouts.OverlappingPanelsValue
import tmg.flashback.ui.components.layouts.rememberOverlappingPanelsState
import tmg.flashback.ui.components.navigation.NavigationBar
import tmg.flashback.ui.components.navigation.NavigationColumn
import tmg.flashback.ui.components.navigation.NavigationTimelineItem
import tmg.flashback.ui.components.navigation.appBarHeight
import tmg.flashback.ui.dashboard.menu.DashboardMenuExpandedScreen
import tmg.flashback.ui.dashboard.menu.DashboardMenuScreen

@Composable
fun DashboardScreen(
    windowSize: WindowSizeClass,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val currentlySelectedItem = viewModel.outputs.currentlySelectedItem.observeAsState(MenuItem.Calendar)
    val seasonScreenItemsList = viewModel.outputs.seasonScreenItemsList.observeAsState(emptyList())
    val appFeatureItemsList = viewModel.outputs.appFeatureItemsList.observeAsState(emptyList())

    val seasonItemsList = viewModel.outputs.seasonsItemsList.observeAsState(emptyList())
    val currentlySelectedSeason = viewModel.outputs.currentlySelectedSeason.observeAsState(0)

    val darkMode = viewModel.outputs.isDarkMode.observeAsState(false)

    val featurePromptList = viewModel.outputs.featurePromptsList.observeAsState(emptyList())

    val appVersion = viewModel.outputs.appVersion.observeAsState("")

    DashboardScreen(
        windowSize = windowSize,
        currentlySelectedItem = currentlySelectedItem.value,
        appFeatureItemsList = appFeatureItemsList.value,
        seasonScreenItemsList = seasonScreenItemsList.value,
        menuItemClicked = viewModel.inputs::clickItem,
        darkMode = darkMode.value,
        darkModeClicked = viewModel.inputs::clickDarkMode,
        featurePromptList = featurePromptList.value,
        featurePromptClicked = viewModel.inputs::clickFeaturePrompt,
        seasonItemsList = seasonItemsList.value,
        seasonClicked = viewModel.inputs::clickSeason,
        appVersion = appVersion.value
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DashboardScreen(
    windowSize: WindowSizeClass,
    currentlySelectedItem: MenuItem,
    appFeatureItemsList: List<MenuItem>,
    seasonScreenItemsList: List<MenuItem>,
    menuItemClicked: (MenuItem) -> Unit,
    darkMode: Boolean,
    darkModeClicked: (Boolean) -> Unit,
    featurePromptList: List<DashboardFeaturePrompt>,
    featurePromptClicked: (DashboardFeaturePrompt) -> Unit,
    seasonItemsList: List<NavigationTimelineItem>,
    seasonClicked: (Int) -> Unit,
    appVersion: String
) {
    val panelsState = rememberOverlappingPanelsState(OverlappingPanelsValue.Closed)

    val coroutineScope = rememberCoroutineScope()
    DisposableEffect(windowSize, effect = {
        coroutineScope.launch { panelsState.closePanels() }
        return@DisposableEffect this.onDispose {  }
    })

    Scaffold(
        modifier = Modifier
            .background(AppTheme.colors.backgroundContainer)
            .navigationBarsPadding()
            .statusBarsPadding(),
        bottomBar = {
            if (windowSize.widthSizeClass == WindowWidthSizeClass.Compact) {
                val position = animateDpAsState(targetValue = if (panelsState.isStartPanelOpen) appBarHeight else 0.dp)
                NavigationBar(
                    modifier = Modifier.offset(y = position.value),
                    list = seasonScreenItemsList.map { it.toNavigationItem(currentlySelectedItem == it) },
                    itemClicked = {

                    }
                )
            }
        },
        content = {
            OverlappingPanels(
                panelsState = panelsState,
                gesturesEnabled = windowSize.widthSizeClass == WindowWidthSizeClass.Compact,
                panelStart = {
                    DashboardMenuScreen(
                        currentlySelectedItem = currentlySelectedItem,
                        appFeatureItemsList = appFeatureItemsList,
                        menuItemClicked = menuItemClicked,
                        darkMode = darkMode,
                        darkModeClicked = darkModeClicked,
                        featurePromptList = featurePromptList,
                        featurePromptClicked = featurePromptClicked,
                        seasonItemsList = seasonItemsList,
                        seasonClicked = seasonClicked,
                        appVersion = appVersion
                    )
                },
                panelCenter = {
                    Row(Modifier.fillMaxSize()) {
                        if (windowSize.widthSizeClass == WindowWidthSizeClass.Medium) {
                            DashboardMenuExpandedScreen(
                                currentlySelectedItem = currentlySelectedItem,
                                appFeatureItemsList = appFeatureItemsList,
                                menuItemClicked = menuItemClicked,
                                darkMode = darkMode,
                                darkModeClicked = darkModeClicked,
                                featurePromptList = featurePromptList,
                                featurePromptClicked = featurePromptClicked,
                                seasonItemsList = seasonItemsList,
                                seasonClicked = seasonClicked,
                                appVersion = appVersion,
                                seasonScreenItemsList = seasonScreenItemsList,
                                lockExpanded = false
                            )
                        }
                        if (windowSize.widthSizeClass == WindowWidthSizeClass.Expanded) {
                            DashboardMenuExpandedScreen(
                                currentlySelectedItem = currentlySelectedItem,
                                appFeatureItemsList = appFeatureItemsList,
                                menuItemClicked = menuItemClicked,
                                darkMode = darkMode,
                                darkModeClicked = darkModeClicked,
                                featurePromptList = featurePromptList,
                                featurePromptClicked = featurePromptClicked,
                                seasonItemsList = seasonItemsList,
                                seasonClicked = seasonClicked,
                                appVersion = appVersion,
                                seasonScreenItemsList = seasonScreenItemsList,
                                lockExpanded = true
                            )
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .background(Color.Cyan))
                        }
                    }
                }
            )
        }
    )
}