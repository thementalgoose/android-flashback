package tmg.flashback.settings.ui.settings.support

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import tmg.flashback.settings.R
import tmg.flashback.ui.components.analytics.ScreenView
import tmg.flashback.ui.components.settings.SettingsScreen

@Composable
fun SettingsSupportScreenVM(
    actionUpClicked: () -> Unit
) {
    ScreenView(screenName = "Settings Support")
    
    val viewModel = viewModel<SettingsSupportViewModel>()

    SettingsScreen(
        title = stringResource(id = R.string.settings_all_support),
        actionUpClicked = actionUpClicked,
        viewModel = viewModel
    )
}