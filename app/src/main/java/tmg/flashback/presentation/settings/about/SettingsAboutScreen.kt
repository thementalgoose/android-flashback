package tmg.flashback.presentation.settings.about

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpSize
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.googleanalytics.presentation.ScreenView
import tmg.flashback.presentation.settings.Settings
import tmg.flashback.strings.R.string
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.components.header.HeaderAction
import tmg.flashback.ui.components.settings.Footer
import tmg.flashback.ui.components.settings.Header
import tmg.flashback.ui.components.settings.Pref
import tmg.flashback.ui.components.settings.Switch
import tmg.flashback.ui.settings.Setting

@Composable
fun SettingsAboutScreenVM(
    actionUpClicked: () -> Unit = { },
    windowSizeClass: WindowSizeClass,
    paddingValues: PaddingValues,
    viewModel: SettingsAboutViewModel = hiltViewModel()
) {
    ScreenView(screenName = "Settings - About")

    val shakeToReportEnabled = viewModel.outputs.shakeToReportEnabled.collectAsState(false)
    SettingsAboutScreen(
        actionUpClicked = actionUpClicked,
        windowSizeClass = windowSizeClass,
        paddingValues = paddingValues,
        prefClicked = viewModel.inputs::prefClicked,
        shakeToReportEnabled = shakeToReportEnabled.value
    )
}

@Composable
fun SettingsAboutScreen(
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    paddingValues: PaddingValues,
    prefClicked: (Setting) -> Unit,
    shakeToReportEnabled: Boolean
) {
    LazyColumn(
        contentPadding = paddingValues,
        content = {
            item("header") {
                Header(
                    text = stringResource(id = string.settings_section_about_title),
                    action = if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) HeaderAction.BACK else null,
                    actionUpClicked = actionUpClicked
                )
            }

            Header(title = string.settings_header_about)
            Pref(
                model = Settings.Other.aboutThisApp,
                onClick = prefClicked
            )
            Pref(
                model = Settings.Other.review,
                onClick = prefClicked
            )
            Switch(
                model = Settings.Other.shakeToReport(shakeToReportEnabled),
                onClick = prefClicked
            )
            Pref(
                model = Settings.Other.buildVersion,
                onClick = prefClicked
            )

            Header(title = string.settings_header_reset)
            Pref(
                model = Settings.Other.resetFirstTimeSync,
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
        SettingsAboutScreen(
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize.Unspecified),
            actionUpClicked = {},
            prefClicked = {},
            paddingValues = PaddingValues.Absolute(),
            shakeToReportEnabled = true
        )
    }
}