package tmg.flashback.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

fun ViewModel.getScope(provider: CoroutineScope?): CoroutineScope = when (provider) {
    null -> viewModelScope
    else -> provider
}