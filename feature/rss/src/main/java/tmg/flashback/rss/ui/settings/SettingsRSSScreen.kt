package tmg.flashback.rss.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.koin.androidx.compose.viewModel
import tmg.flashback.rss.R
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.components.settings.SettingsScreen

@Composable
fun SettingsRSSScreenVM(
    actionUpClicked: () -> Unit
) {
    val viewModel by viewModel<SettingsRSSViewModel>()
    viewModel.loadSettings()
    val list = viewModel.settings.observeAsState(emptyList())

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundPrimary),
        content = {
            item("header") {
                Header(
                    text = stringResource(id = R.string.settings_rss_title),
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
        }
    )
}