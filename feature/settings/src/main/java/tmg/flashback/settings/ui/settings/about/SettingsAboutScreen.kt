package tmg.flashback.settings.ui.settings.about

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import tmg.flashback.settings.R
import tmg.flashback.ui.components.analytics.ScreenView
import tmg.flashback.ui.components.settings.SettingsScreen

@Composable
fun SettingsAboutScreenVM(
    actionUpClicked: () -> Unit
) {
    ScreenView(screenName = "Settings About")
    
    val viewModel = viewModel<SettingsAboutViewModel>()

    SettingsScreen(
        title = stringResource(id = R.string.settings_about),
        actionUpClicked = actionUpClicked,
        viewModel = viewModel
    )
}