package tmg.flashback.ui.settings.appearance.nightmode

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
import tmg.flashback.ui.model.NightMode

@Composable
fun NightModeScreenVM(
    viewModel: NightModeViewModel = hiltViewModel(),
    dismiss: () -> Unit,
) {
    val nightMode = viewModel.outputs.currentlySelected.collectAsState(initial = NightMode.DEFAULT)
    NightModeScreen(
        currentlySelectedNightMode = nightMode.value,
        nightModeClicked = viewModel.inputs::selectNightMode,
        dismiss = dismiss
    )
}

@Composable
fun NightModeScreen(
    currentlySelectedNightMode: NightMode,
    nightModeClicked: (NightMode) -> Unit,
    dismiss: () -> Unit,
) {
    BottomSheet(
        title = stringResource(id = R.string.settings_theme_nightmode_title),
        subtitle = stringResource(id = R.string.settings_theme_nightmode_description)
    ) {
        NightMode.values().forEach { nightMode ->
            InputSelection(
                modifier = Modifier.padding(
                    horizontal = AppTheme.dimens.medium,
                    vertical = AppTheme.dimens.xsmall
                ),
                label = stringResource(id = nightMode.label),
                icon = nightMode.icon,
                isSelected = nightMode == currentlySelectedNightMode,
                itemClicked = {
                    nightModeClicked(nightMode)
                    dismiss()
                }
            )
        }
    }
}