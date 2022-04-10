package tmg.flashback.ui.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

@Composable
fun <T> Observe(
    liveData: LiveData<T>,
    callback: (T) -> Unit,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
) {
    LaunchedEffect(Unit) {
        liveData.observe(lifecycleOwner, callback)
    }
}