package tmg.flashback.ads.config.ui.settings.adverts

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.ads.config.R
import tmg.flashback.ui.components.analytics.ScreenView
import tmg.flashback.ui.components.settings.SettingsScreen

@Composable
fun SettingsAdvertScreenVM(
    actionUpClicked: () -> Unit
) {
    ScreenView(screenName = "Settings Adverts")
    
    val viewModel = hiltViewModel<SettingsAdvertViewModel>()

    SettingsScreen(
        title = stringResource(id = R.string.settings_help_adverts_title),
        actionUpClicked = actionUpClicked,
        viewModel = viewModel
    )
}