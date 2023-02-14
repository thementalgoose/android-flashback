@file:OptIn(ExperimentalMaterial3Api::class)

package tmg.flashback.ui.dashboard

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.window.layout.WindowLayoutInfo
import kotlinx.coroutines.launch
import tmg.flashback.debug.model.DebugMenuItem
import tmg.flashback.eastereggs.model.MenuIcons
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.AppGraph
import tmg.flashback.ui.components.layouts.OverlappingPanels
import tmg.flashback.ui.components.layouts.OverlappingPanelsValue
import tmg.flashback.ui.components.layouts.rememberOverlappingPanelsState
import tmg.flashback.ui.components.navigation.NavigationBar
import tmg.flashback.ui.components.navigation.NavigationTimelineItem
import tmg.flashback.ui.components.navigation.appBarHeight
import tmg.flashback.ui.dashboard.menu.DashboardMenuExpandedScreen
import tmg.flashback.ui.dashboard.menu.DashboardMenuScreen
import tmg.flashback.ui.navigation.Navigator

@Composable
fun DashboardScreen(
    windowSize: WindowSizeClass,
    windowLayoutInfo: WindowLayoutInfo,
    navigator: Navigator,
    closeApp: () -> Unit,
    navViewModel: DashboardNavViewModel = hiltViewModel(),
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val currentlySelectedItem = navViewModel.outputs.currentlySelectedItem.observeAsState(MenuItem.Calendar)
    val seasonScreenItemsList = navViewModel.outputs.seasonScreenItemsList.observeAsState(emptyList())
    val appFeatureItemsList = navViewModel.outputs.appFeatureItemsList.observeAsState(emptyList())
    val debugMenuItems = navViewModel.outputs.debugMenuItems.observeAsState(emptyList())

    val seasonItemsList = navViewModel.outputs.seasonsItemsList.observeAsState(emptyList())
    val currentlySelectedSeason = navViewModel.outputs.currentlySelectedSeason.observeAsState(0)

    val showBottomBar = navViewModel.outputs.showBottomBar.observeAsState(true)
    val showMenu = navViewModel.outputs.showMenu.observeAsState(false)

    val darkMode = viewModel.outputs.isDarkMode.observeAsState(false)
    val featurePromptList = viewModel.outputs.featurePromptsList.observeAsState(emptyList())
    val appVersion = viewModel.outputs.appVersion.observeAsState("")

    val snow = viewModel.outputs.snow.observeAsState(false)
    val titleIcon = viewModel.outputs.titleIcon.observeAsState(null)

    DashboardScreen(
        windowSize = windowSize,
        windowLayoutInfo = windowLayoutInfo,
        navigator = navigator,
        closeApp = closeApp,
        currentlySelectedItem = currentlySelectedItem.value,
        appFeatureItemsList = appFeatureItemsList.value,
        seasonScreenItemsList = seasonScreenItemsList.value,
        debugMenuItems = debugMenuItems.value,
        debugMenuItemClicked = navViewModel.inputs::clickDebug,
        menuItemClicked = navViewModel.inputs::clickItem,
        showBottomBar = showBottomBar.value,
        showMenu = showMenu.value,
        darkMode = darkMode.value,
        darkModeClicked = viewModel.inputs::clickDarkMode,
        featurePromptList = featurePromptList.value,
        featurePromptClicked = viewModel.inputs::clickFeaturePrompt,
        seasonItemsList = seasonItemsList.value,
        seasonClicked = navViewModel.inputs::clickSeason,
        appVersion = appVersion.value,
        easterEggSnow = snow.value,
        easterEggTitleIcon = titleIcon.value
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DashboardScreen(
    windowSize: WindowSizeClass,
    windowLayoutInfo: WindowLayoutInfo,
    navigator: Navigator,
    closeApp: () -> Unit,
    currentlySelectedItem: MenuItem,
    appFeatureItemsList: List<MenuItem>,
    seasonScreenItemsList: List<MenuItem>,
    debugMenuItems: List<DebugMenuItem>,
    debugMenuItemClicked: (DebugMenuItem) -> Unit,
    menuItemClicked: (MenuItem) -> Unit,
    showBottomBar: Boolean,
    showMenu: Boolean,
    darkMode: Boolean,
    darkModeClicked: (Boolean) -> Unit,
    featurePromptList: List<FeaturePrompt>,
    featurePromptClicked: (FeaturePrompt) -> Unit,
    seasonItemsList: List<NavigationTimelineItem>,
    seasonClicked: (Int) -> Unit,
    appVersion: String,
    easterEggSnow: Boolean,
    easterEggTitleIcon: MenuIcons?
) {
    val panelsState = rememberOverlappingPanelsState(OverlappingPanelsValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    // Close panel if window size is changes via. configuration change
    DisposableEffect(windowSize, effect = {
        coroutineScope.launch { panelsState.closePanels() }
        return@DisposableEffect this.onDispose {  }
    })

    // Close the menu if we shouldn't be showing it
    DisposableEffect(showMenu, effect = {
        if (!showMenu) {
            coroutineScope.launch { panelsState.closePanels() }
        }
        return@DisposableEffect this.onDispose {  }
    })

    val openMenu: () -> Unit = {
        coroutineScope.launch { panelsState.openStartPanel() }
    }

    // Close the menu if bck is pressed on the menu
    BackHandler(panelsState.isStartPanelOpen) {
        coroutineScope.launch {
            panelsState.closePanels()
        }
    }

    Scaffold(
        modifier = Modifier
            .background(AppTheme.colors.backgroundContainer)
            .navigationBarsPadding()
            .statusBarsPadding(),
        bottomBar = {
            if (windowSize.widthSizeClass == WindowWidthSizeClass.Compact) {
                val position = animateDpAsState(targetValue = if (panelsState.isStartPanelOpen || !showBottomBar) appBarHeight else 0.dp)
                NavigationBar(
                    modifier = Modifier.offset(y = position.value),
                    list = seasonScreenItemsList.map { it.toNavigationItem(currentlySelectedItem == it) },
                    itemClicked = { item ->
                        menuItemClicked(seasonScreenItemsList.first { it.id == item.id })
                    }
                )
            }
        },
        content = {
            OverlappingPanels(
                modifier = Modifier.background(AppTheme.colors.backgroundContainer),
                panelsState = panelsState,
                gesturesEnabled = windowSize.widthSizeClass == WindowWidthSizeClass.Compact && showMenu,
                panelStart = {
                    DashboardMenuScreen(
                        closeMenu = {
                            coroutineScope.launch { panelsState.closePanels() }
                        },
                        currentlySelectedItem = currentlySelectedItem,
                        appFeatureItemsList = appFeatureItemsList,
                        debugMenuItems = debugMenuItems,
                        debugMenuItemClicked = debugMenuItemClicked,
                        menuItemClicked = menuItemClicked,
                        darkMode = darkMode,
                        darkModeClicked = darkModeClicked,
                        featurePromptList = featurePromptList,
                        featurePromptClicked = featurePromptClicked,
                        seasonItemsList = seasonItemsList,
                        seasonClicked = seasonClicked,
                        appVersion = appVersion,
                        easterEggSnow = easterEggSnow,
                        easterEggTitleIcon = easterEggTitleIcon
                    )
                },
                panelCenter = {
                    Row(Modifier.fillMaxSize()) {
                        if (windowSize.widthSizeClass == WindowWidthSizeClass.Medium) {
                            DashboardMenuExpandedScreen(
                                currentlySelectedItem = currentlySelectedItem,
                                appFeatureItemsList = appFeatureItemsList,
                                seasonScreenItemsList = seasonScreenItemsList,
                                debugMenuItems = debugMenuItems,
                                debugMenuItemClicked = debugMenuItemClicked,
                                menuItemClicked = menuItemClicked,
                                darkMode = darkMode,
                                darkModeClicked = darkModeClicked,
                                featurePromptList = featurePromptList,
                                featurePromptClicked = featurePromptClicked,
                                seasonItemsList = seasonItemsList,
                                seasonClicked = seasonClicked,
                                appVersion = appVersion,
                                easterEggSnow = easterEggSnow,
                                easterEggTitleIcon = easterEggTitleIcon,
                                lockExpanded = false
                            )
                        }
                        if (windowSize.widthSizeClass == WindowWidthSizeClass.Expanded) {
                            DashboardMenuExpandedScreen(
                                currentlySelectedItem = currentlySelectedItem,
                                appFeatureItemsList = appFeatureItemsList,
                                seasonScreenItemsList = seasonScreenItemsList,
                                debugMenuItems = debugMenuItems,
                                debugMenuItemClicked = debugMenuItemClicked,
                                menuItemClicked = menuItemClicked,
                                darkMode = darkMode,
                                darkModeClicked = darkModeClicked,
                                featurePromptList = featurePromptList,
                                featurePromptClicked = featurePromptClicked,
                                seasonItemsList = seasonItemsList,
                                seasonClicked = seasonClicked,
                                appVersion = appVersion,
                                easterEggSnow = easterEggSnow,
                                easterEggTitleIcon = easterEggTitleIcon,
                                lockExpanded = true
                            )
                        }
                        Row(modifier = Modifier
                            .weight(1f)
                            .background(AppTheme.colors.backgroundContainer)
                        ) {
                            if (windowSize.widthSizeClass != WindowWidthSizeClass.Compact) {
                                Box(
                                    Modifier
                                        .width(1.dp)
                                        .fillMaxHeight()
                                        .background(AppTheme.colors.backgroundSecondary))
                            }
                            AppGraph(
                                modifier = Modifier.weight(1f),
                                openMenu = openMenu,
                                windowSize = windowSize,
                                windowInfo = windowLayoutInfo,
                                navigator = navigator,
                                closeApp = closeApp
                            )
                        }
                    }
                }
            )
        }
    )
}