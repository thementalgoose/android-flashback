package tmg.flashback.ui.extensions

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
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
        println("Launched Effect")
        liveData.observe(lifecycleOwner, callback)
    }
}

@Composable
fun ObserveContext(
    liveData: LiveData<Unit>,
    callback: (Context) -> Unit,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    val context = LocalContext.current
    Observe(
        liveData = liveData,
        lifecycleOwner = lifecycleOwner,
        callback = { callback(context) }
    )
}

@Composable
fun <T> ObserveContext(
    liveData: LiveData<T>,
    callback: (T, Context) -> Unit,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    val context = LocalContext.current
    Observe(
        liveData = liveData,
        lifecycleOwner = lifecycleOwner,
        callback = { item ->
            callback(item, context)
        }
    )
}