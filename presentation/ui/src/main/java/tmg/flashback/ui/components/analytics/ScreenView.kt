package tmg.flashback.ui.components.analytics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ScreenView(
    screenName: String,
    args: Map<String, String> = mapOf(),
    updateKey: Any? = Unit
) {
    val viewModel = viewModel<ScreenViewViewModel>()
    DisposableEffect(key1 = updateKey, effect = {
        viewModel.viewScreen(screenName, args)
        this.onDispose { }
    })
}