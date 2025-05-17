package tmg.flashback.presentation.settings.about

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpSize
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.googleanalytics.presentation.ScreenView
import tmg.flashback.presentation.settings.Settings
import tmg.flashback.strings.R.string
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.ui.components.header.HeaderAction
import tmg.flashback.ui.components.settings.Footer
import tmg.flashback.ui.components.settings.Header
import tmg.flashback.ui.components.settings.Pref
import tmg.flashback.ui.components.settings.Switch
import tmg.flashback.ui.settings.Setting

@Composable
fun SettingsPrivacyScreenVM(
    actionUpClicked: () -> Unit = { },
    windowSizeClass: WindowSizeClass,
    paddingValues: PaddingValues,
    viewModel: SettingsPrivacyViewModel = hiltViewModel()
) {
    ScreenView(screenName = "Settings - Privacy")

    val crashReportingEnabled = viewModel.outputs.crashReportingEnabled.collectAsState(false)
    val analyticsEnabled = viewModel.outputs.analyticsEnabled.collectAsState(false)

    SettingsPrivacyScreen(
        actionUpClicked = actionUpClicked,
        windowSizeClass = windowSizeClass,
        paddingValues = paddingValues,
        prefClicked = viewModel.inputs::prefClicked,
        crashReportingEnabled = crashReportingEnabled.value,
        analyticsEnabled = analyticsEnabled.value
    )
}

@Composable
fun SettingsPrivacyScreen(
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    prefClicked: (Setting) -> Unit,
    paddingValues: PaddingValues,
    crashReportingEnabled: Boolean,
    analyticsEnabled: Boolean
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = paddingValues,
        content = {
            item("header") {
                tmg.flashback.ui.components.header.Header(
                    text = stringResource(id = string.settings_section_privacy_title),
                    action = if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) HeaderAction.BACK else null,
                    actionUpClicked = actionUpClicked
                )
            }

            Header(title = string.settings_header_legal)
            Pref(
                model = Settings.Other.privacyPolicy,
                onClick = prefClicked
            )
            Header(title = string.settings_header_device_info)
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
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize.Unspecified),
            actionUpClicked = {},
            prefClicked = {},
            paddingValues = PaddingValues.Absolute(),
            crashReportingEnabled = true,
            analyticsEnabled = false
        )
    }
}