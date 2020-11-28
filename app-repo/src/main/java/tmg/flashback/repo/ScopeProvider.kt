package tmg.flashback.repo

import kotlinx.coroutines.CoroutineScope

interface ScopeProvider {
    fun getCoroutineScope(): CoroutineScope?
}