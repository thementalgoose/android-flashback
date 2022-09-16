@file:JvmName("ConfigureRSS_ScreenKt")

package tmg.flashback.rss.ui.configure

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.rss.R
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.ui.components.settings.Header
import tmg.flashback.ui.components.settings.Pref
import tmg.flashback.ui.navigation.Screen

@Composable
fun ConfigureRSSScreenVM(
    actionUpClicked: () -> Unit
) {
    val viewModel = hiltViewModel<ConfigureRSSViewModel>()

    val showDescriptionEnabled = viewModel.outputs.showDescriptionEnabled.observeAsState(true)
    val showCustomAdd = viewModel.outputs.showAddCustom.observeAsState(false)
    val sources = viewModel.outputs.rssSources.observeAsState(emptyList())

    ConfigureRSSScreen(
        actionUpClicked = actionUpClicked
    )
}

@Composable
fun ConfigureRSSScreen(
    actionUpClicked: () -> Unit,
    showDescriptionEnabled: Boolean,
    showCustomAdd: Boolean,
    sources: List<RSSSource>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundPrimary),
        content = {
            item(key = "header") {
                tmg.flashback.ui.components.header.Header(
                    text = stringResource(id = R.string.settings_rss_configure_sources_title),
                    icon = painterResource(id = R.drawable.ic_back),
                    iconContentDescription = stringResource(id = R.string.ab_back),
                    actionUpClicked = actionUpClicked
                )
            }
            Header(title = R.string.settings_header_layout)
            Header(title = R.string.settings_header_rss_list)
            Header(title = R.string.settings_header_custom)
        }
    )
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        ConfigureRSSScreen(
            actionUpClicked = {}
        )
    }
}