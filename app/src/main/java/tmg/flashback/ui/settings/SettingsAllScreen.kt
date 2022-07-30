package tmg.flashback.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.R
import tmg.flashback.ui.components.analytics.ScreenView
import tmg.flashback.ui.components.settings.SettingsScreen

@Composable
fun SettingsAllScreenVM(
    actionUpClicked: () -> Unit
) {
    ScreenView(screenName = "Settings All")
    
    val viewModel = hiltViewModel<SettingsAllViewModel>()

    SettingsScreen(
        title = stringResource(id = R.string.settings_all_title),
        actionUpClicked = actionUpClicked,
        viewModel = viewModel
    )
}