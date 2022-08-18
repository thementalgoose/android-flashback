package tmg.flashback.settings.ui.settings.support

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.settings.R
import tmg.flashback.ui.components.analytics.ScreenView
import tmg.flashback.ui.components.settings.SettingsScreen

@Composable
fun SettingsSupportScreenVM(
    actionUpClicked: () -> Unit
) {
    ScreenView(screenName = "Settings Support")
    
    val viewModel = hiltViewModel<SettingsSupportViewModel>()

    SettingsScreen(
        title = stringResource(id = R.string.settings_all_support),
        actionUpClicked = actionUpClicked,
        viewModel = viewModel
    )
}