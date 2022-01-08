package tmg.flashback.ads.ui.settings.adverts

import tmg.flashback.ads.R
import tmg.flashback.ads.controller.AdsController
import tmg.flashback.ui.settings.SettingsModel
import tmg.flashback.ui.settings.SettingsViewModel

//region Inputs

interface SettingsAdvertViewModelInputs {

}

//endregion

//region Outputs

interface SettingsAdvertViewModelOutputs {

}

//endregion


class SettingsAdvertViewModel(
    private val advertController: AdsController
): SettingsViewModel(), SettingsAdvertViewModelInputs, SettingsAdvertViewModelOutputs {

    override val models: List<SettingsModel> get() = mutableListOf<SettingsModel>().apply {
        add(SettingsModel.Header(R.string.settings_help_adverts_title))
        add(SettingsModel.SwitchPref(
            title = R.string.settings_help_adverts_title,
            description = R.string.settings_help_adverts_description,
            getState = { advertController.userConfigAdvertsEnabled },
            saveState = { advertController.userConfigAdvertsEnabled = it }
        ))
    }

    var inputs: SettingsAdvertViewModelInputs = this
    var outputs: SettingsAdvertViewModelOutputs = this

    init {

    }

    //region Inputs

    //endregion

    //region Outputs

    //endregion
}
