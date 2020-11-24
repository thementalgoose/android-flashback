package tmg.flashback.testutils

import kotlinx.coroutines.CoroutineScope

class TestScopeProvider(
        private val scope: CoroutineScope
): ScopeProvider {
    override fun getCoroutineScope(): CoroutineScope? = scope
}