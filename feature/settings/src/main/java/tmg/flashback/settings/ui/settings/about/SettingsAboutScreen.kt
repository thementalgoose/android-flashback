package tmg.flashback.settings.ui.settings.about

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.koin.androidx.compose.viewModel
import tmg.flashback.settings.R
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.components.settings.SettingsScreen

@Composable
fun SettingsAboutScreenVM(
    actionUpClicked: () -> Unit
) {
    val viewModel by viewModel<SettingsAboutViewModel>()
    val list = viewModel.settings.observeAsState(emptyList())

    LazyColumn(content = {
        item("header") {
            Header(
                text = stringResource(id = R.string.settings_about),
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