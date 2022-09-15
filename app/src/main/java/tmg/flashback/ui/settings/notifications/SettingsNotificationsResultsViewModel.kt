package tmg.flashback.ui.settings.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tmg.flashback.ui.settings.Setting

interface SettingsNotificationsResultsViewModelInputs {
    fun prefClicked(pref: Setting)
}

interface SettingsNotificationsResultsViewModelOutputs {
    val permissionEnabled: LiveData<Boolean>
    val qualifyingEnabled: LiveData<Boolean>
    val sprintEnabled: LiveData<Boolean>
    val raceEnabled: LiveData<Boolean>
}

class SettingsNotificationsResultsViewModel: ViewModel(), SettingsNotificationsResultsViewModelInputs, SettingsNotificationsResultsViewModelOutputs {

    val inputs: SettingsNotificationsResultsViewModelInputs = this
    val outputs: SettingsNotificationsResultsViewModelOutputs = this

    override val permissionEnabled: MutableLiveData<Boolean> = MutableLiveData()
    override val qualifyingEnabled: MutableLiveData<Boolean> = MutableLiveData()
    override val sprintEnabled: MutableLiveData<Boolean> = MutableLiveData()
    override val raceEnabled: MutableLiveData<Boolean> = MutableLiveData()

    override fun prefClicked(pref: Setting) {
        when (pref.key) {

        }
    }
}