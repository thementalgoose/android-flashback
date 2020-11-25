package tmg.flashback.di.async

import kotlinx.coroutines.CoroutineScope
import tmg.flashback.repo.ScopeProvider

class ViewModelScopeProvider: ScopeProvider {
    override fun getCoroutineScope(): CoroutineScope? = null
}