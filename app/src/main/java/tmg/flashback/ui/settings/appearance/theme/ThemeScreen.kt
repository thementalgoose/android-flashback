package tmg.flashback.ui.settings.appearance.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.R
import tmg.flashback.ui.components.layouts.BottomSheet
import tmg.flashback.ui.model.Theme

@Composable
fun ThemeScreenVM(
    viewModel: ThemeViewModel = hiltViewModel()
) {
    val currentTheme = viewModel.outputs.currentlySelected.observeAsState(initial = Theme.DEFAULT)
    ThemeScreen(
        currentlySelectedTheme = currentTheme.value,
        themeClicked = viewModel.inputs::selectTheme
    )
}

@Composable
fun ThemeScreen(
    currentlySelectedTheme: Theme,
    themeClicked: (Theme) -> Unit,
) {
    BottomSheet(
        title = stringResource(id = R.string.settings_theme_theme_title),
        subtitle = stringResource(id = R.string.settings_theme_theme_description)
    ) {

    }
}