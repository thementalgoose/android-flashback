package tmg.flashback.presentation.dashboard

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStore
import androidx.navigation.compose.rememberNavController
import androidx.window.layout.WindowLayoutInfo
import kotlinx.coroutines.launch
import tmg.flashback.ads.ads.components.AdvertProvider
import tmg.flashback.debug.model.DebugMenuItem
import tmg.flashback.eastereggs.model.MenuIcons
import tmg.flashback.googleanalytics.presentation.ScreenView
import tmg.flashback.navigation.Navigator
import tmg.flashback.style.AppTheme
import tmg.flashback.presentation.AppGraph
import tmg.flashback.ui.components.layouts.OverlappingPanels
import tmg.flashback.ui.components.layouts.OverlappingPanelsValue
import tmg.flashback.ui.components.layouts.rememberOverlappingPanelsState
import tmg.flashback.ui.components.navigation.NavigationBar
import tmg.flashback.ui.components.navigation.appBarHeight
import tmg.flashback.presentation.dashboard.menu.DashboardMenuExpandedScreen
import tmg.flashback.presentation.dashboard.menu.DashboardMenuScreen
import tmg.flashback.ui.components.layouts.OverlappingPanelsState
import tmg.flashback.ui.foldables.getFoldingConfig

@Composable
fun DashboardScreen(
    windowSize: WindowSizeClass,
    viewModelStore: ViewModelStore,
    windowLayoutInfo: WindowLayoutInfo,
    advertProvider: AdvertProvider,
    navigator: Navigator,
    closeApp: () -> Unit,
    navViewModel: DashboardNavViewModel = hiltViewModel(),
    viewModel: DashboardViewModel = hiltViewModel(),
    deeplink: String?
) {
    val navController = rememberNavController()
    DisposableEffect(key1 = navController, effect = {
        Log.i("Dashboard", "Configuring navController to viewmodel")
        navigator.navController = navController
        navController.setViewModelStore(viewModelStore)
        navController.addOnDestinationChangedListener(navViewModel)
        return@DisposableEffect onDispose {  }
    })

    val currentlySelectedItem = navViewModel.outputs.currentlySelectedItem.collectAsState(MenuItem.Calendar)
    val seasonScreenItemsList = navViewModel.outputs.seasonScreenItemsList.collectAsState(emptyList())
    val appFeatureItemsList = navViewModel.outputs.appFeatureItemsList.collectAsState(emptyList())
    val debugMenuItems = navViewModel.outputs.debugMenuItems.collectAsState(emptyList())

    val showBottomBar = navViewModel.outputs.showBottomBar.collectAsState(true)
    val showMenu = navViewModel.outputs.showMenu.collectAsState(false)

    val darkMode = viewModel.outputs.isDarkMode.collectAsState(false)
    val featurePromptList = viewModel.outputs.featurePromptsList.collectAsState(emptyList())
    val appVersion = viewModel.outputs.appVersion.collectAsState("")

    val snow = viewModel.outputs.snow.collectAsState(false)
    val titleIcon = viewModel.outputs.titleIcon.collectAsState(null)
    val ukraine = viewModel.outputs.ukraine.collectAsState(false)

    DashboardScreen(
        windowSize = windowSize,
        windowLayoutInfo = windowLayoutInfo,
        advertProvider = advertProvider,
        navigator = navigator,
        deeplink = deeplink,
        closeApp = closeApp,
        currentlySelectedItem = currentlySelectedItem.value,
        appFeatureItemsList = appFeatureItemsList.value,
        seasonScreenItemsList = seasonScreenItemsList.value,
        debugMenuItems = debugMenuItems.value,
        debugMenuItemClicked = navViewModel.inputs::clickDebug,
        menuItemClicked = navViewModel.inputs::clickItem,
        isRoot = navViewModel.inputs::navigationInRoot,
        showBottomBar = showBottomBar.value,
        showMenu = showMenu.value,
        darkMode = darkMode.value,
        darkModeClicked = viewModel.inputs::clickDarkMode,
        featurePromptList = featurePromptList.value,
        featurePromptClicked = viewModel.inputs::clickFeaturePrompt,
        appVersion = appVersion.value,
        easterEggSnow = snow.value,
        easterEggTitleIcon = titleIcon.value,
        easterEggUkraine = ukraine.value
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DashboardScreen(
    windowSize: WindowSizeClass,
    windowLayoutInfo: WindowLayoutInfo,
    advertProvider: AdvertProvider,
    navigator: Navigator,
    deeplink: String?,
    closeApp: () -> Unit,
    currentlySelectedItem: MenuItem,
    appFeatureItemsList: List<MenuItem>,
    seasonScreenItemsList: List<MenuItem>,
    debugMenuItems: List<DebugMenuItem>,
    debugMenuItemClicked: (DebugMenuItem) -> Unit,
    menuItemClicked: (MenuItem) -> Unit,
    isRoot: (String, Boolean) -> Unit,
    showBottomBar: Boolean,
    showMenu: Boolean,
    darkMode: Boolean,
    darkModeClicked: (Boolean) -> Unit,
    featurePromptList: List<FeaturePrompt>,
    featurePromptClicked: (FeaturePrompt) -> Unit,
    appVersion: String,
    easterEggSnow: Boolean,
    easterEggTitleIcon: MenuIcons?,
    easterEggUkraine: Boolean,
) {
    ScreenView(screenName = "Dashboard")

    val panelsState = rememberOverlappingPanelsState(OverlappingPanelsValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val foldingConfig = windowLayoutInfo.getFoldingConfig()
    val openMenu: () -> Unit = {
        coroutineScope.launch { panelsState.openStartPanel() }
    }

    val navigationBarPosition = animateDpAsState(
        targetValue = if (panelsState.isStartPanelOpen || !showBottomBar) appBarHeight else 0.dp,
        label = "navigationBarPosition"
    )
    Scaffold(
        modifier = Modifier
            .background(AppTheme.colors.backgroundContainer)
            .navigationBarsPadding()
            .statusBarsPadding(),
        bottomBar = {
            if (windowSize.widthSizeClass == WindowWidthSizeClass.Compact) {
                NavigationBar(
                    modifier = Modifier.offset(y = navigationBarPosition.value),
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
                panelsState = when (windowSize.widthSizeClass == WindowWidthSizeClass.Compact) {
                    true -> panelsState
                    false -> OverlappingPanelsState(OverlappingPanelsValue.Closed)
                },
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
                        appVersion = appVersion,
                        easterEggSnow = easterEggSnow,
                        easterEggTitleIcon = easterEggTitleIcon,
                        easterEggUkraine = easterEggUkraine
                    )
                },
                panelCenter = {
                    Row(Modifier.fillMaxSize()) {
                        if (windowSize.widthSizeClass == WindowWidthSizeClass.Medium) {
                            DashboardMenuExpandedScreen(
                                foldingConfig = foldingConfig,
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
                                appVersion = appVersion,
                                easterEggSnow = easterEggSnow,
                                easterEggTitleIcon = easterEggTitleIcon,
                                easterEggUkraine = easterEggUkraine,
                                lockExpanded = false,
                                initialExpandedState = false
                            )
                        }
                        if (windowSize.widthSizeClass == WindowWidthSizeClass.Expanded) {
                            DashboardMenuExpandedScreen(
                                foldingConfig = foldingConfig,
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
                                appVersion = appVersion,
                                easterEggSnow = easterEggSnow,
                                easterEggTitleIcon = easterEggTitleIcon,
                                easterEggUkraine = easterEggUkraine,
                                lockExpanded = false,
                                initialExpandedState = false
                            )
                        }
                        Row(modifier = Modifier
                            .weight(1f)
                            .background(AppTheme.colors.backgroundContainer)
                        ) {
                            AppGraph(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(
                                        bottom = when (windowSize.widthSizeClass == WindowWidthSizeClass.Compact) {
                                            true -> appBarHeight - navigationBarPosition.value
                                            false -> 0.dp
                                        }
                                    ),
                                advertProvider = advertProvider,
                                deeplink = deeplink,
                                isRoot = isRoot,
                                navController = navigator.navController,
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

    // Close panel if window size is changes via. configuration change
    DisposableEffect(windowSize) {
        println("Orientation change - Close panels")
        coroutineScope.launch { panelsState.closePanels() }
        return@DisposableEffect onDispose { }
    }

    // Close the menu if we shouldn't be showing it
    LaunchedEffect(showMenu, block = {
        if (!showMenu) {
            panelsState.closePanels()
        }
    })

    // Close the menu if bck is pressed on the menu
    BackHandler(panelsState.isStartPanelOpen) {
        coroutineScope.launch {
            panelsState.closePanels()
        }
    }
}

