package tmg.flashback.stats.ui.settings.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.koin.androidx.compose.viewModel
import tmg.flashback.stats.R
import tmg.flashback.ui.components.settings.SettingsScreen

@Composable
fun SettingsHomeScreenVM(
    actionUpClicked: () -> Unit
) {
    val viewModel by viewModel<SettingsHomeViewModel>()

    SettingsScreen(
        title = stringResource(id = R.string.settings_home),
        actionUpClicked = actionUpClicked,
        viewModel = viewModel
    )
}