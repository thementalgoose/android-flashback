package tmg.flashback.rss.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import tmg.flashback.repo.ScopeProvider

abstract class RSSBaseViewModel(
    scopeProvider: ScopeProvider
): ViewModel() {

    val scope: CoroutineScope = when (val provider = scopeProvider.getCoroutineScope()) {
        null -> viewModelScope
        else -> provider
    }
}