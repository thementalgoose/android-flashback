package tmg.flashback.reactiongame.presentation

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ReactionScreenVM(
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    viewModel: ReactionViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    ReactionScreen(
        actionUpClicked = actionUpClicked,
        windowSizeClass = windowSizeClass,
        state = uiState.value
    )
}

@Composable
private fun ReactionScreen(
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    state: ReactionUiState,
) {

}