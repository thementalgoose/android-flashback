package tmg.flashback.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

interface ScopeProvider {
    fun get(): CoroutineContext
}