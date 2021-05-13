package tmg.common.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tmg.utilities.lifecycle.DataEvent

abstract class SettingsViewModel: ViewModel() {

    abstract val models: List<SettingsModel>

    private val _settings: MutableLiveData<List<SettingsModel>> = MutableLiveData()
    val settings: LiveData<List<SettingsModel>> = _settings

    private val _clickPref: MutableLiveData<DataEvent<SettingsModel.Pref>> = MutableLiveData()
    val clickPref: LiveData<DataEvent<SettingsModel.Pref>> = _clickPref

    private val _switchPref: MutableLiveData<DataEvent<Pair<SettingsModel.SwitchPref, Boolean>>> = MutableLiveData()
    val switchPref: LiveData<DataEvent<Pair<SettingsModel.SwitchPref, Boolean>>> = _switchPref

    init {
        refreshList()
    }

    fun clickPreference(model: SettingsModel.Pref) {
        _clickPref.value = DataEvent(model)
    }

    //region Outputs

    fun clickSwitchPreference(model: SettingsModel.SwitchPref, toState: Boolean) {
        model.saveState(toState)
        refreshList()
        _switchPref.value = DataEvent(Pair(model, toState))
    }

    //endregion

    private fun refreshList() {
        _settings.value = models
    }
}
