package tmg.flashback.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.koin.androidx.compose.viewModel
import tmg.flashback.R
import tmg.flashback.ui.components.settings.SettingsScreen

@Composable
fun SettingsAllScreenVM(
    actionUpClicked: () -> Unit
) {
    val viewModel by viewModel<SettingsAllViewModel>()

    SettingsScreen(
        title = stringResource(id = R.string.settings_all_title),
        actionUpClicked = actionUpClicked,
        viewModel = viewModel
    )
}