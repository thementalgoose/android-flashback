package tmg.flashback.ads.ui.settings.adverts

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.koin.androidx.compose.viewModel
import tmg.flashback.ads.R
import tmg.flashback.ui.components.settings.SettingsScreen

@Composable
fun SettingsAdvertScreenVM(
    actionUpClicked: () -> Unit
) {
    val viewModel by viewModel<SettingsAdvertViewModel>()

    SettingsScreen(
        title = stringResource(id = R.string.settings_help_adverts_title),
        actionUpClicked = actionUpClicked,
        viewModel = viewModel
    )
}