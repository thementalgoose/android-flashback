package tmg.flashback.ui.settings.web

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
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
import tmg.flashback.ui.components.settings.Footer
import tmg.flashback.ui.components.settings.Header
import tmg.flashback.ui.components.settings.Switch
import tmg.flashback.ui.settings.Setting
import tmg.flashback.ui.settings.Settings

@Composable
fun SettingsWebScreenVM(
    showBack: Boolean = true,
    actionUpClicked: () -> Unit = { },
    viewModel: SettingsWebViewModel = hiltViewModel()
) {
    ScreenView(screenName = "Settings - Web")

    val webBrowserEnabled = viewModel.outputs.enable.collectAsState(false)
    val javascriptEnabled = viewModel.outputs.enableJavascript.collectAsState(false)

    SettingsWebScreen(
        showBack = showBack,
        actionUpClicked = actionUpClicked,
        prefClicked = viewModel.inputs::prefClicked,
        webBrowserEnabled = webBrowserEnabled.value,
        javascriptEnabled = javascriptEnabled.value
    )
}

@Composable
fun SettingsWebScreen(
    showBack: Boolean,
    actionUpClicked: () -> Unit,
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
                    icon = when (showBack) {
                        true -> painterResource(id = R.drawable.ic_back)
                        false -> null
                    },
                    iconContentDescription = stringResource(id = R.string.ab_back),
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
            showBack = true,
            actionUpClicked = {},
            prefClicked = {},
            webBrowserEnabled = false,
            javascriptEnabled = true
        )
    }
}