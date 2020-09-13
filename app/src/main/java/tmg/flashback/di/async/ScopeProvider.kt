package tmg.flashback.di.async

import kotlinx.coroutines.CoroutineScope

interface ScopeProvider {
    fun getCoroutineScope(): CoroutineScope?
}