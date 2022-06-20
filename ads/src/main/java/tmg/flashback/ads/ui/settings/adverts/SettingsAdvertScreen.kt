package tmg.flashback.ads.ui.settings.adverts

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.koin.androidx.compose.viewModel
import tmg.flashback.ads.R
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.components.settings.SettingsScreen

@Composable
fun SettingsAdvertScreenVM(
    actionUpClicked: () -> Unit
) {
    val viewModel by viewModel<SettingsAdvertViewModel>()
    val list = viewModel.settings.observeAsState(emptyList())

    LazyColumn(content = {
        item("header") {
            Header(
                text = stringResource(id = R.string.settings_help_adverts_title),
                icon = painterResource(id = R.drawable.ic_back),
                iconContentDescription = stringResource(id = R.string.ab_back),
                actionUpClicked = actionUpClicked
            )
        }
        item("settings") {
            SettingsScreen(
                models = list.value,
                prefClicked = viewModel::clickPreference,
                prefSwitchClicked = viewModel::clickSwitchPreference
            )
        }
    })
}