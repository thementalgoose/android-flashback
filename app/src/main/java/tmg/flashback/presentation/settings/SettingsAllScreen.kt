package tmg.flashback.presentation.settings

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import tmg.flashback.googleanalytics.presentation.ScreenView
import tmg.flashback.presentation.settings.about.SettingsAboutScreenVM
import tmg.flashback.presentation.settings.about.SettingsPrivacyScreenVM
import tmg.flashback.presentation.settings.ads.SettingsAdsScreenVM
import tmg.flashback.presentation.settings.appearance.nightmode.SettingsNightModeScreenVM
import tmg.flashback.presentation.settings.appearance.theme.SettingsThemeScreenVM
import tmg.flashback.presentation.settings.data.SettingsLayoutScreenVM
import tmg.flashback.presentation.settings.data.SettingsWeatherScreenVM
import tmg.flashback.presentation.settings.notifications.SettingsNotificationUpcomingNoticeScreenVM
import tmg.flashback.presentation.settings.web.SettingsWebScreenVM
import tmg.flashback.presentation.settings.widgets.SettingsWidgetScreenVM
import tmg.flashback.rss.contract.RSSNavigationComponent
import tmg.flashback.rss.contract.requireRSSNavigationComponent
import tmg.flashback.strings.R.string
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.components.header.HeaderAction
import tmg.flashback.ui.components.layouts.MasterDetailsPane
import tmg.flashback.ui.components.settings.Footer
import tmg.flashback.ui.components.settings.Header
import tmg.flashback.ui.components.settings.Pref
import tmg.flashback.ui.components.settings.Section
import tmg.flashback.ui.lifecycle.OnLifecycleEvent
import tmg.flashback.ui.settings.Setting

@Composable
fun SettingsAllScreenVM(
    paddingValues: PaddingValues,
    actionUpClicked: () -> Unit = { },
    windowSizeClass: WindowSizeClass,
    isRoot: (Boolean) -> Unit,
    viewModel: SettingsAllViewModel = hiltViewModel(),
    rssNavigationComponent: RSSNavigationComponent = requireRSSNavigationComponent()
) {
    ScreenView(screenName = "Settings")

    val uiState = viewModel.outputs.uiState.collectAsState()
    LaunchedEffect(uiState.value.selectedSubScreen != null, block = {
        isRoot(uiState.value.selectedSubScreen != null)
    })
    OnLifecycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> viewModel.refresh()
            else -> { }
        }
    }
    LaunchedEffect(uiState) {
        viewModel.refresh()
    }

    MasterDetailsPane(
        windowSizeClass = windowSizeClass,
        master = {
            SettingsAllScreen(
                paddingValues = paddingValues,
                windowSizeClass = windowSizeClass,
                actionUpClicked = actionUpClicked,
                prefClicked = viewModel.inputs::itemClicked,
                uiState = uiState.value
            )
        },
        detailsShow = uiState.value.selectedSubScreen != null,
        detailsActionUpClicked = viewModel.inputs::back,
        details = {
            when (uiState.value.selectedSubScreen) {
                null -> { /* Render nothing */ }
                SettingsAllViewModel.SettingsScreen.DARK_MODE -> {
                    SettingsNightModeScreenVM(
                        actionUpClicked = viewModel.inputs::back,
                        windowSizeClass = windowSizeClass,
                        paddingValues = paddingValues,
                    )
                }
                SettingsAllViewModel.SettingsScreen.THEME -> {
                    SettingsThemeScreenVM(
                        actionUpClicked = viewModel.inputs::back,
                        windowSizeClass = windowSizeClass,
                        paddingValues = paddingValues,
                    )
                }
                SettingsAllViewModel.SettingsScreen.LAYOUT -> {
                    SettingsLayoutScreenVM(
                        actionUpClicked = viewModel.inputs::back,
                        windowSizeClass = windowSizeClass,
                        paddingValues = paddingValues,
                    )
                }
                SettingsAllViewModel.SettingsScreen.WEATHER -> {
                    SettingsWeatherScreenVM(
                        actionUpClicked = viewModel.inputs::back,
                        windowSizeClass = windowSizeClass,
                        paddingValues = paddingValues,
                    )
                }
                SettingsAllViewModel.SettingsScreen.RSS_CONFIGURE -> {
                    rssNavigationComponent.Configure(
                        actionUpClicked = viewModel.inputs::back,
                        windowSizeClass = windowSizeClass,
                        paddingValues = paddingValues,
                    )
                }
                SettingsAllViewModel.SettingsScreen.WEB_BROWSER -> {
                    SettingsWebScreenVM(
                        actionUpClicked = viewModel.inputs::back,
                        windowSizeClass = windowSizeClass,
                        paddingValues = paddingValues,
                    )
                }
                SettingsAllViewModel.SettingsScreen.NOTIFICATIONS_TIMER -> {
                    SettingsNotificationUpcomingNoticeScreenVM(
                        actionUpClicked = viewModel.inputs::back,
                        windowSizeClass = windowSizeClass,
                        paddingValues = paddingValues,
                    )
                }
                SettingsAllViewModel.SettingsScreen.WIDGETS -> {
                    SettingsWidgetScreenVM(
                        actionUpClicked = viewModel.inputs::back,
                        windowSizeClass = windowSizeClass,
                        paddingValues = paddingValues,
                    )
                }
                SettingsAllViewModel.SettingsScreen.ADS -> {
                    SettingsAdsScreenVM(
                        actionUpClicked = viewModel.inputs::back,
                        windowSizeClass = windowSizeClass,
                        paddingValues = paddingValues,
                    )
                }
                SettingsAllViewModel.SettingsScreen.PRIVACY -> {
                    SettingsPrivacyScreenVM(
                        actionUpClicked = viewModel.inputs::back,
                        windowSizeClass = windowSizeClass,
                        paddingValues = paddingValues,
                    )
                }
                SettingsAllViewModel.SettingsScreen.ABOUT -> {
                    SettingsAboutScreenVM(
                        actionUpClicked = viewModel.inputs::back,
                        windowSizeClass = windowSizeClass,
                        paddingValues = paddingValues,
                    )
                }
            }
        }
    )
}

@Composable
fun SettingsAllScreen(
    paddingValues: PaddingValues,
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    prefClicked: (Setting) -> Unit,
    uiState: SettingsAllViewModel.UiState,
) {
    LazyColumn(
        contentPadding = paddingValues,
        content = {
            item("header") {
                Header(
                    text = stringResource(id = string.settings_title),
                    action = if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) HeaderAction.MENU else null,
                    actionUpClicked = actionUpClicked
                )
            }

            Header(title = string.settings_header_appearance)
            Section(
                model = Settings.Theme.darkMode,
                onClick = prefClicked
            )
            if (uiState.themeEnabled) {
                Section(
                    model = Settings.Theme.theme,
                    onClick = prefClicked
                )
            }
            Header(title = string.settings_header_data)
            Section(
                model = Settings.Data.layout,
                onClick = prefClicked
            )
            Section(
                model = Settings.Data.weather,
                onClick = prefClicked
            )
            if (uiState.rssEnabled) {
                Header(title = string.settings_header_rss_feed)
                Section(
                    model = Settings.RSS.rss,
                    onClick = prefClicked
                )
            }
            Header(title = string.settings_header_web_browser)
            Section(
                model = Settings.Web.inAppBrowser,
                onClick = prefClicked
            )
            Header(title = string.settings_header_notifications)
            Section(
                model = Settings.Notifications.notificationResults,
                onClick = prefClicked
            )
            Section(
                model = Settings.Notifications.notificationUpcoming,
                onClick = prefClicked
            )
            Pref(
                model = Settings.Notifications.notificationUpcomingNotice,
                onClick = prefClicked
            )
            Header(title = string.settings_header_widgets)
            Section(
                model = Settings.Widgets.widgets,
                onClick = prefClicked
            )
            if (uiState.adsEnabled) {
                Header(title = string.settings_header_ads)
                Section(
                    model = Settings.Ads.ads,
                    onClick = prefClicked
                )
            }
            Header(title = string.settings_header_other)
            Section(
                model = Settings.Other.privacy,
                onClick = prefClicked
            )
            Section(
                model = Settings.Other.about,
                onClick = prefClicked
            )
            Footer()
        }
    )
}


@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        SettingsAllScreen(
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize.Unspecified),
            actionUpClicked = {},
            prefClicked = {},
            paddingValues = PaddingValues.Absolute(),
            uiState = SettingsAllViewModel.UiState(
                adsEnabled = true,
                rssEnabled = true,
                themeEnabled = true,
                notificationRuntimePermission = true,
                notificationExactAlarmPermission = true
            )
        )
    }
}


@PreviewTheme
@Composable
private fun PreviewAllHidden() {
    AppThemePreview {
        SettingsAllScreen(
            actionUpClicked = {},
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize.Unspecified),
            prefClicked = {},
            paddingValues = PaddingValues.Absolute(),
            uiState = SettingsAllViewModel.UiState(
                adsEnabled = false,
                rssEnabled = false,
                themeEnabled = false,
                notificationRuntimePermission = false,
                notificationExactAlarmPermission = false
            )
        )
    }
}