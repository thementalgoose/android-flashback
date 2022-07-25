package tmg.flashback.web.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.koin.androidx.compose.viewModel
import tmg.flashback.ui.components.analytics.ScreenView
import tmg.flashback.ui.components.settings.SettingsScreen
import tmg.flashback.web.R

@Composable
fun SettingsWebBrowserScreenVM(
    actionUpClicked: () -> Unit
) {
    ScreenView(screenName = "Settings Web Browser")

    val viewModel by viewModel<SettingsWebBrowserViewModel>()

    SettingsScreen(
        title = stringResource(id = R.string.settings_web_browser_title),
        actionUpClicked = actionUpClicked,
        viewModel = viewModel
    )
}