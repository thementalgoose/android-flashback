package tmg.flashback.presentation.settings.web

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
import tmg.flashback.ui.components.header.HeaderAction
import tmg.flashback.ui.components.settings.Footer
import tmg.flashback.ui.components.settings.Header
import tmg.flashback.ui.components.settings.Switch
import tmg.flashback.ui.settings.Setting

@Composable
fun SettingsWebScreenVM(
    actionUpClicked: () -> Unit = { },
    paddingValues: PaddingValues,
    windowSizeClass: WindowSizeClass,
    viewModel: SettingsWebViewModel = hiltViewModel()
) {
    ScreenView(screenName = "Settings - Web")

    val webBrowserEnabled = viewModel.outputs.enable.collectAsState(false)
    val javascriptEnabled = viewModel.outputs.enableJavascript.collectAsState(false)

    SettingsWebScreen(
        actionUpClicked = actionUpClicked,
        windowSizeClass = windowSizeClass,
        paddingValues = paddingValues,
        prefClicked = viewModel.inputs::prefClicked,
        webBrowserEnabled = webBrowserEnabled.value,
        javascriptEnabled = javascriptEnabled.value
    )
}

@Composable
fun SettingsWebScreen(
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    paddingValues: PaddingValues,
    prefClicked: (Setting) -> Unit,
    webBrowserEnabled: Boolean,
    javascriptEnabled: Boolean
) {
    LazyColumn(
        contentPadding = paddingValues,
        content = {
            item("header") {
                tmg.flashback.ui.components.header.Header(
                    text = stringResource(id = string.settings_section_web_browser_title),
                    action = if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) HeaderAction.BACK else null,
                    actionUpClicked = actionUpClicked
                )
            }

            Header(title = string.settings_header_in_app_browser)
            Switch(
                model = Settings.Web.enable(webBrowserEnabled),
                onClick = prefClicked
            )
            Switch(
                model = Settings.Web.javascript(javascriptEnabled, isEnabled = webBrowserEnabled),
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
        SettingsWebScreen(
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize.Unspecified),
            paddingValues = PaddingValues.Absolute(),
            actionUpClicked = {},
            prefClicked = {},
            webBrowserEnabled = false,
            javascriptEnabled = true
        )
    }
}