package tmg.common.ui.settings.all

import tmg.common.ui.settings.SettingsModel
import tmg.common.ui.settings.SettingsViewModel

//region Inputs

interface SettingsAllViewModelInputs {

}

//endregion

//region Outputs

interface SettingsAllViewModelOutputs {

}

//endregion


class SettingsAllViewModel: SettingsViewModel(), SettingsAllViewModelInputs, SettingsAllViewModelOutputs {

    override val models: List<SettingsModel> = listOf(
        SettingsModel.Header(R.string.setting)
    )

    var inputs: SettingsAllViewModelInputs = this
    var outputs: SettingsAllViewModelOutputs = this

    init {

    }

    //region Inputs

    //endregion

    //region Outputs

    //endregion
}
