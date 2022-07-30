package tmg.flashback.ui.components.analytics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ScreenView(
    screenName: String,
    args: Map<String, String> = mapOf(),
    updateKey: Any? = Unit
) {
    val viewModel = hiltViewModel<ScreenViewViewModel>()
    DisposableEffect(key1 = updateKey, effect = {
        viewModel.viewScreen(screenName, args)
        this.onDispose { }
    })
}