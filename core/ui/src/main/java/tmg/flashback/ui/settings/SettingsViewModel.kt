package tmg.flashback.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class SettingsViewModel: ViewModel() {

    abstract val models: List<SettingsModel>

    private val _settings: MutableLiveData<List<SettingsModel>> = MutableLiveData()
    val settings: LiveData<List<SettingsModel>> = _settings

    fun loadSettings() {
        refreshList()
    }

    fun clickPreference(model: SettingsModel.Pref) {
        model.onClick?.invoke()
    }

    //region Outputs

    fun clickSwitchPreference(model: SettingsModel.SwitchPref, toState: Boolean) {
        model.saveState(toState)
        model.saveStateNotification?.invoke(toState)
        refreshList()
    }

    //endregion

    private fun refreshList() {
        _settings.value = models
    }
}
