package tmg.flashback.ui.settings.appearance.theme

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.R
import tmg.flashback.style.AppTheme
import tmg.flashback.style.input.InputSelection
import tmg.flashback.ui.components.layouts.BottomSheet
import tmg.flashback.ui.extensions.icon
import tmg.flashback.ui.extensions.label
import tmg.flashback.ui.model.Theme

@Composable
fun ThemeScreenVM(
    viewModel: ThemeViewModel = hiltViewModel(),
    dismiss: () -> Unit
) {
    val currentTheme = viewModel.outputs.currentlySelected.collectAsState(initial = Theme.DEFAULT)
    ThemeScreen(
        currentlySelectedTheme = currentTheme.value,
        themeClicked = viewModel.inputs::selectTheme,
        dismiss = dismiss
    )
}

@Composable
fun ThemeScreen(
    currentlySelectedTheme: Theme,
    themeClicked: (Theme) -> Unit,
    dismiss: () -> Unit,
) {
    BottomSheet(
        title = stringResource(id = R.string.settings_theme_theme_title),
        subtitle = stringResource(id = R.string.settings_theme_theme_description),
        backClicked = dismiss
    ) {
        Theme.values().forEach { theme ->
            InputSelection(
                modifier = Modifier.padding(
                    horizontal = AppTheme.dimens.medium,
                    vertical = AppTheme.dimens.xsmall
                ),
                label = stringResource(id = theme.label),
                icon = theme.icon,
                isSelected = theme == currentlySelectedTheme,
                itemClicked = {
                    themeClicked(theme)
                    dismiss()
                }
            )
        }
    }
}