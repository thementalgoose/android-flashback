package tmg.flashback.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.R
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.ui.components.analytics.ScreenView
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.components.settings.Footer
import tmg.flashback.ui.components.settings.Header
import tmg.flashback.ui.components.settings.Section

@Composable
fun SettingsAllScreenVM(
    showMenu: Boolean = true,
    actionUpClicked: () -> Unit = { },
    viewModel: SettingsAllViewModel = hiltViewModel()
) {
    ScreenView(screenName = "Settings")

    val isThemeEnabled = viewModel.outputs.isThemeEnabled.collectAsState(false)
    val isAdsEnabled = viewModel.outputs.isAdsEnabled.collectAsState(false)
    val isRSSEnabled = viewModel.outputs.isRSSEnabled.collectAsState(false)

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    SettingsAllScreen(
        showMenu = showMenu,
        actionUpClicked = actionUpClicked,
        prefClicked = viewModel.inputs::itemClicked,
        isThemeEnabled = isThemeEnabled.value,
        isAdsEnabled = isAdsEnabled.value,
        isRSSEnabled = isRSSEnabled.value
    )
}

@Composable
fun SettingsAllScreen(
    showMenu: Boolean,
    actionUpClicked: () -> Unit,
    prefClicked: (Setting) -> Unit,
    isThemeEnabled: Boolean,
    isAdsEnabled: Boolean,
    isRSSEnabled: Boolean
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppTheme.colors.backgroundPrimary),
        content = {
            item("header") {
                Header(
                    text = stringResource(id = R.string.settings_title),
                    icon = when (showMenu) {
                        true -> painterResource(id = R.drawable.ic_menu)
                        false -> null
                    },
                    iconContentDescription = stringResource(id = R.string.ab_menu),
                    actionUpClicked = actionUpClicked
                )
            }

            Header(title = R.string.settings_header_appearance)
            Section(
                model = Settings.Theme.darkMode,
                onClick = prefClicked
            )
            if (isThemeEnabled) {
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
            if (isRSSEnabled) {
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
            Header(title = R.string.settings_header_notifications)
            Section(
                model = Settings.Notifications.notificationUpcoming,
                onClick = prefClicked
            )
            Section(
                model = Settings.Notifications.notificationResults,
                onClick = prefClicked
            )
            if (isAdsEnabled) {
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
            isThemeEnabled = true,
            isAdsEnabled = true,
            isRSSEnabled = true
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewNoTheme() {
    AppThemePreview {
        SettingsAllScreen(
            showMenu = true,
            actionUpClicked = {},
            prefClicked = {},
            isThemeEnabled = false,
            isAdsEnabled = false,
            isRSSEnabled = false
        )
    }
}