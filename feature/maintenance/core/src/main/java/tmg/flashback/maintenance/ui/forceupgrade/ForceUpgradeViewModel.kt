package tmg.flashback.maintenance.ui.forceupgrade

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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
    val title: LiveData<String>
    val message: LiveData<String>
    val showLink: LiveData<Pair<String, String>?> // linkText, link
}

//endregion

@HiltViewModel
internal class ForceUpgradeViewModel @Inject constructor(
    private val maintenanceRepository: MaintenanceRepository,
    private val resetConfigUseCase: ResetConfigUseCase
): ViewModel(), ForceUpgradeViewModelInputs, ForceUpgradeViewModelOutputs {

    var inputs: ForceUpgradeViewModelInputs = this
    var outputs: ForceUpgradeViewModelOutputs = this

    override val title: MutableLiveData<String> = MutableLiveData()
    override val message: MutableLiveData<String> = MutableLiveData()
    override val showLink: MutableLiveData<Pair<String, String>?> = MutableLiveData()

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
