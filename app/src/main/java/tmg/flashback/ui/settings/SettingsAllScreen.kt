package tmg.flashback.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import tmg.flashback.R
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.googleanalytics.presentation.ScreenView
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.components.header.HeaderAction
import tmg.flashback.ui.components.settings.Footer
import tmg.flashback.ui.components.settings.Header
import tmg.flashback.ui.components.settings.Pref
import tmg.flashback.ui.components.settings.Section
import tmg.flashback.ui.lifecycle.OnLifecycleEvent

@Composable
fun SettingsAllScreenVM(
    showMenu: Boolean = true,
    actionUpClicked: () -> Unit = { },
    viewModel: SettingsAllViewModel = hiltViewModel()
) {
    ScreenView(screenName = "Settings")

    val uiState = viewModel.outputs.uiState.collectAsState()

    OnLifecycleEvent { owner, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> viewModel.refresh()
            else -> { }
        }
    }

    LaunchedEffect(uiState) {
        viewModel.refresh()
    }

    SettingsAllScreen(
        showMenu = showMenu,
        actionUpClicked = actionUpClicked,
        prefClicked = viewModel.inputs::itemClicked,
        uiState = uiState.value
    )
}

@Composable
fun SettingsAllScreen(
    showMenu: Boolean,
    actionUpClicked: () -> Unit,
    prefClicked: (Setting) -> Unit,
    uiState: SettingsAllViewModel.UiState,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppTheme.colors.backgroundPrimary),
        content = {
            item("header") {
                Header(
                    text = stringResource(id = R.string.settings_title),
                    action = if (showMenu) HeaderAction.MENU else null,
                    actionUpClicked = actionUpClicked
                )
            }

            Header(title = R.string.settings_header_appearance)
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
            Header(title = R.string.settings_header_data)
            Section(
                model = Settings.Data.layout,
                onClick = prefClicked
            )
            Section(
                model = Settings.Data.weather,
                onClick = prefClicked
            )
            if (uiState.rssEnabled) {
                Header(title = R.string.settings_header_rss_feed)
                Section(
                    model = Settings.RSS.rss,
                    onClick = prefClicked
                )
            }
            Header(title = R.string.settings_header_web_browser)
            Section(
                model = Settings.Web.inAppBrowser,
                onClick = prefClicked
            )
            Header(title = R.string.settings_header_notifications_results_available)
            if (!uiState.notificationRuntimePermission) {
                Pref(
                    model = Settings.Notifications.notificationPermissionEnable,
                    onClick = prefClicked
                )
            }
            Section(
                model = Settings.Notifications.notificationResults(isEnabled = uiState.notificationRuntimePermission),
                onClick = prefClicked
            )
            Header(title = R.string.settings_header_notifications_upcoming)
            if (!uiState.notificationExactAlarmPermission || !uiState.notificationRuntimePermission) {
                if (uiState.notificationExactAlarmPermission) {
                    Pref(
                        model = Settings.Notifications.notificationPermissionEnable,
                        onClick = prefClicked
                    )
                } else {
                    Pref(
                        model = Settings.Notifications.notificationExactAlarmEnable,
                        onClick = prefClicked
                    )
                }
            }
            Section(
                model = Settings.Notifications.notificationUpcoming(isEnabled = uiState.notificationExactAlarmPermission && uiState.notificationExactAlarmPermission),
                onClick = prefClicked
            )
            Pref(
                model = Settings.Notifications.notificationUpcomingNotice(isEnabled = uiState.notificationExactAlarmPermission && uiState.notificationExactAlarmPermission),
                onClick = prefClicked
            )
            if (uiState.adsEnabled) {
                Header(title = R.string.settings_header_ads)
                Section(
                    model = Settings.Ads.ads,
                    onClick = prefClicked
                )
            }
            Header(title = R.string.settings_header_other)
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
            showMenu = true,
            actionUpClicked = {},
            prefClicked = {},
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
            showMenu = true,
            actionUpClicked = {},
            prefClicked = {},
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