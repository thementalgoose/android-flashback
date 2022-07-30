package tmg.flashback.stats.ui.settings.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.stats.R
import tmg.flashback.ui.components.analytics.ScreenView
import tmg.flashback.ui.components.settings.SettingsScreen

@Composable
fun SettingsHomeScreenVM(
    actionUpClicked: () -> Unit
) {
    val viewModel = hiltViewModel<SettingsHomeViewModel>()

    ScreenView(screenName = "Settings Home")

    SettingsScreen(
        title = stringResource(id = R.string.settings_home),
        actionUpClicked = actionUpClicked,
        viewModel = viewModel
    )
}