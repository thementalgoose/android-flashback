package tmg.flashback.settings.ui.settings.appearance

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import tmg.flashback.settings.R
import tmg.flashback.ui.components.analytics.ScreenView
import tmg.flashback.ui.components.settings.SettingsScreen

@Composable
fun SettingsAppearanceScreenVM(
    actionUpClicked: () -> Unit
) {
    ScreenView(screenName = "Settings Appearance")
    
    val viewModel = viewModel<SettingsAppearanceViewModel>()

    SettingsScreen(
        title = stringResource(id = R.string.settings_all_appearance),
        actionUpClicked = actionUpClicked,
        viewModel = viewModel
    )
}