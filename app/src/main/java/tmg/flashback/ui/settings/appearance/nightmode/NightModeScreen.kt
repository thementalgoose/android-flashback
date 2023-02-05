package tmg.flashback.ui.settings.appearance.nightmode

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.R
import tmg.flashback.ui.components.layouts.BottomSheet
import tmg.flashback.ui.model.NightMode
import tmg.flashback.ui.model.Theme

@Composable
fun NightModeScreenVM(
    viewModel: NightModeViewModel = hiltViewModel()
) {
    val nightMode = viewModel.outputs.currentlySelected.observeAsState(initial = NightMode.DEFAULT)
    NightModeScreen(
        currentlySelectedNightMode = nightMode.value,
        nightModeClicked = viewModel.inputs::selectNightMode
    )
}

@Composable
fun NightModeScreen(
    currentlySelectedNightMode: NightMode,
    nightModeClicked: (NightMode) -> Unit,
) {
    BottomSheet(
        title = stringResource(id = R.string.settings_theme_nightmode_title),
        subtitle = stringResource(id = R.string.settings_theme_nightmode_description)
    ) {

    }
}