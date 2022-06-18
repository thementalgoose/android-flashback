package tmg.flashback.rss.ui.settings

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.koin.androidx.compose.viewModel
import tmg.flashback.rss.R
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.components.settings.SettingsScreen

@Composable
fun SettingsRSSScreenVM(
    actionUpClicked: () -> Unit
) {
    val viewModel by viewModel<SettingsRSSViewModel>()
    val list = viewModel.settings.observeAsState(emptyList())

    LazyColumn(content = {
        item("header") {
            Header(
                text = stringResource(id = R.string.title_rss),
                icon = painterResource(id = R.drawable.ic_back),
                iconContentDescription = stringResource(id = R.string.ab_back),
                actionUpClicked = actionUpClicked
            )
        }
        item("settings") {
            SettingsScreen(models = list.value)
        }
    })
}