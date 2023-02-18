package tmg.flashback.ui.settings.about

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.R
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.ui.components.analytics.ScreenView
import tmg.flashback.ui.components.settings.Footer
import tmg.flashback.ui.components.settings.Header
import tmg.flashback.ui.components.settings.Pref
import tmg.flashback.ui.components.settings.Switch
import tmg.flashback.ui.settings.Setting
import tmg.flashback.ui.settings.Settings

@Composable
fun SettingsPrivacyScreenVM(
    showBack: Boolean = true,
    actionUpClicked: () -> Unit = { },
    viewModel: SettingsPrivacyViewModel = hiltViewModel()
) {
    ScreenView(screenName = "Settings - Privacy")

    val crashReportingEnabled = viewModel.outputs.crashReportingEnabled.observeAsState(false)
    val analyticsEnabled = viewModel.outputs.analyticsEnabled.observeAsState(false)

    SettingsPrivacyScreen(
        showBack = showBack,
        actionUpClicked = actionUpClicked,
        prefClicked = viewModel.inputs::prefClicked,
        crashReportingEnabled = crashReportingEnabled.value,
        analyticsEnabled = analyticsEnabled.value
    )
}

@Composable
fun SettingsPrivacyScreen(
    showBack: Boolean,
    actionUpClicked: () -> Unit,
    prefClicked: (Setting) -> Unit,
    crashReportingEnabled: Boolean,
    analyticsEnabled: Boolean
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundPrimary),
        content = {
            item("header") {
                tmg.flashback.ui.components.header.Header(
                    text = stringResource(id = R.string.settings_section_privacy_title),
                    icon = when (showBack) {
                        true -> painterResource(id = R.drawable.ic_back)
                        false -> null
                    },
                    iconContentDescription = stringResource(id = R.string.ab_back),
                    actionUpClicked = actionUpClicked
                )
            }

            Header(title = R.string.settings_header_legal)
            Pref(
                model = Settings.Other.privacyPolicy,
                onClick = prefClicked
            )
            Header(title = R.string.settings_header_device_info)
            Switch(
                model = Settings.Other.crashReporting(crashReportingEnabled),
                onClick = prefClicked
            )
            Switch(
                model = Settings.Other.analytics(analyticsEnabled),
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
        SettingsPrivacyScreen(
            showBack = true,
            actionUpClicked = {},
            prefClicked = {},
            crashReportingEnabled = true,
            analyticsEnabled = false
        )
    }
}