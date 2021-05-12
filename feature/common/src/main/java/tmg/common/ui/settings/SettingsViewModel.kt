package tmg.common.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

//region Inputs

interface SettingsViewModelInputs {
    fun
}

//endregion

//region Outputs

interface SettingsViewModelOutputs {
    val settings: LiveData<SettingsModel>
}

//endregion


abstract class SettingsViewModel: ViewModel(), SettingsViewModelInputs, SettingsViewModelOutputs {

    var inputs: SettingsViewModelInputs = this
    var outputs: SettingsViewModelOutputs = this

    init {

    }

    //region Inputs

    //endregion

    //region Outputs

    //endregion
}
