package tmg.flashback.ui.settings.layout

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.R
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.ui.components.settings.Header
import tmg.flashback.ui.components.settings.Switch
import tmg.flashback.ui.settings.Settings
import tmg.flashback.ui.settings.Setting

@Composable
fun SettingsLayoutScreenVM(
    actionUpClicked: () -> Unit
) {
    val viewModel = hiltViewModel<SettingsLayoutViewModel>()

    val menuAllExpandedEnabled = viewModel.outputs.menuAllExpandedEnabled.observeAsState(true)
    val menuFavouriteExpandedEnabled = viewModel.outputs.menuFavouriteExpandedEnabled.observeAsState(true)
    val providedByAtTopEnabled = viewModel.outputs.providedByAtTopEnabled.observeAsState(true)
    SettingsLayoutScreen(
        actionUpClicked = actionUpClicked,
        prefClicked = viewModel.inputs::prefClicked,
        menuAllExpandedEnabled = menuAllExpandedEnabled.value,
        menuFavouriteExpandedEnabled = menuFavouriteExpandedEnabled.value,
        providedByAtTopEnabled = providedByAtTopEnabled.value
    )
}

@Composable
fun SettingsLayoutScreen(
    actionUpClicked: () -> Unit,
    prefClicked: (Setting) -> Unit,
    menuAllExpandedEnabled: Boolean,
    menuFavouriteExpandedEnabled: Boolean,
    providedByAtTopEnabled: Boolean
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        content = {
            item("header") {
                tmg.flashback.ui.components.header.Header(
                    text = stringResource(id = R.string.settings_section_home_title),
                    icon = painterResource(id = R.drawable.ic_back),
                    iconContentDescription = stringResource(id = R.string.ab_back),
                    actionUpClicked = actionUpClicked
                )
            }

            Header(title = R.string.settings_header_menu)
            Switch(
                model = Settings.Layout.menuAllExpanded(menuAllExpandedEnabled),
                onClick = prefClicked
            )
            Switch(
                model = Settings.Layout.menuFavouriteExpanded(menuFavouriteExpandedEnabled),
                onClick = prefClicked
            )

            Header(title = R.string.settings_header_home)
            Switch(
                model = Settings.Layout.providedByAtTop(providedByAtTopEnabled),
                onClick = prefClicked
            )
        }
    )
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        SettingsLayoutScreen(
            actionUpClicked = {},
            prefClicked = {},
            menuAllExpandedEnabled = true,
            menuFavouriteExpandedEnabled = false,
            providedByAtTopEnabled = true
        )
    }
}