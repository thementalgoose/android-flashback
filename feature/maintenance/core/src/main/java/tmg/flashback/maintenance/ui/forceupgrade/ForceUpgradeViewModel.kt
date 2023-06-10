package tmg.flashback.maintenance.ui.forceupgrade

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import tmg.flashback.configuration.usecases.ResetConfigUseCase
import tmg.flashback.maintenance.BuildConfig
import tmg.flashback.maintenance.repository.MaintenanceRepository
import javax.inject.Inject

//region Inputs

interface ForceUpgradeViewModelInputs

//endregion

//region Outputs

interface ForceUpgradeViewModelOutputs {
    val title: StateFlow<String>
    val message: StateFlow<String>
    val showLink: StateFlow<Pair<String, String>?> // linkText, link
}

//endregion

@HiltViewModel
internal class ForceUpgradeViewModel @Inject constructor(
    private val maintenanceRepository: MaintenanceRepository,
    private val resetConfigUseCase: ResetConfigUseCase
): ViewModel(), ForceUpgradeViewModelInputs, ForceUpgradeViewModelOutputs {

    var inputs: ForceUpgradeViewModelInputs = this
    var outputs: ForceUpgradeViewModelOutputs = this

    override val title: MutableStateFlow<String> = MutableStateFlow("")
    override val message: MutableStateFlow<String> = MutableStateFlow("")
    override val showLink: MutableStateFlow<Pair<String, String>?> = MutableStateFlow(null)

    init {
        maintenanceRepository.forceUpgrade?.let {
            title.value = it.title
            message.value = it.message
            showLink.value = it.link
        }
        if (maintenanceRepository.forceUpgrade == null) {
            title.value = "Error :("
            message.value = "Please restart the app"
            showLink.value = null
        }

        viewModelScope.launch {
            resetConfigUseCase.reset()
            if (BuildConfig.DEBUG) {
                Log.i("Force Upgrade", "Force Upgrade reset")
            }
        }
    }
}
