package tmg.flashback.ui.settings.web

import androidx.compose.foundation.background
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
import tmg.flashback.R
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.googleanalytics.presentation.ScreenView
import tmg.flashback.ui.components.header.HeaderAction
import tmg.flashback.ui.components.settings.Footer
import tmg.flashback.ui.components.settings.Header
import tmg.flashback.ui.components.settings.Switch
import tmg.flashback.ui.settings.Setting
import tmg.flashback.ui.settings.Settings

@Composable
fun SettingsWebScreenVM(
    actionUpClicked: () -> Unit = { },
    windowSizeClass: WindowSizeClass,
    viewModel: SettingsWebViewModel = hiltViewModel()
) {
    ScreenView(screenName = "Settings - Web")

    val webBrowserEnabled = viewModel.outputs.enable.collectAsState(false)
    val javascriptEnabled = viewModel.outputs.enableJavascript.collectAsState(false)

    SettingsWebScreen(
        actionUpClicked = actionUpClicked,
        windowSizeClass = windowSizeClass,
        prefClicked = viewModel.inputs::prefClicked,
        webBrowserEnabled = webBrowserEnabled.value,
        javascriptEnabled = javascriptEnabled.value
    )
}

@Composable
fun SettingsWebScreen(
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    prefClicked: (Setting) -> Unit,
    webBrowserEnabled: Boolean,
    javascriptEnabled: Boolean
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundPrimary),
        content = {
            item("header") {
                tmg.flashback.ui.components.header.Header(
                    text = stringResource(id = R.string.settings_section_web_browser_title),
                    action = if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) HeaderAction.BACK else null,
                    actionUpClicked = actionUpClicked
                )
            }

            Header(title = R.string.settings_header_in_app_browser)
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
            actionUpClicked = {},
            prefClicked = {},
            webBrowserEnabled = false,
            javascriptEnabled = true
        )
    }
}