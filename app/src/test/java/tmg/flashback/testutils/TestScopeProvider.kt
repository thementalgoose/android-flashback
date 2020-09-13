package tmg.flashback.testutils

import kotlinx.coroutines.CoroutineScope
import tmg.flashback.di.async.ScopeProvider

class TestScopeProvider: ScopeProvider {
    override fun getCoroutineScope(): CoroutineScope? = null
}