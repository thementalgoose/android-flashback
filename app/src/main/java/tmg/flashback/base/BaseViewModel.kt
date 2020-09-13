package tmg.flashback.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import tmg.flashback.di.async.ScopeProvider
import tmg.flashback.utils.getScope

abstract class BaseViewModel(
    executionScope: ScopeProvider
): ViewModel() {

    val scope: CoroutineScope = getScope(executionScope.getCoroutineScope())
}