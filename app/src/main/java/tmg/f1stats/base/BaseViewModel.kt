package tmg.f1stats.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class BaseViewModel: ViewModel() {

    fun async(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(block = block)
    }
}