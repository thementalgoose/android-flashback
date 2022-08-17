package tmg.flashback.permissions.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import tmg.flashback.permissions.manager.PermissionManager
import javax.inject.Inject

interface RationaleViewModelInputs {
    fun requestPermission(rationale: RationaleType)
}

interface RationaleViewModelOutputs {

}

@HiltViewModel
internal class RationaleViewModel @Inject constructor(
    private val permissionManager: PermissionManager
): ViewModel(), RationaleViewModelInputs, RationaleViewModelOutputs {

    val inputs: RationaleViewModelInputs = this
    val outputs: RationaleViewModelOutputs = this

    override fun requestPermission(rationale: RationaleType) {
        viewModelScope.launch {
            val result = permissionManager.isPermissionGranted(rationale)
            println("Result! $result")
        }
    }
}