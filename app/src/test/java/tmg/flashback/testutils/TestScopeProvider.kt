package tmg.flashback.testutils

import kotlinx.coroutines.CoroutineScope
import tmg.flashback.repo.ScopeProvider

class TestScopeProvider(
        private val scope: CoroutineScope
): ScopeProvider {
    override fun getCoroutineScope(): CoroutineScope? = scope
}