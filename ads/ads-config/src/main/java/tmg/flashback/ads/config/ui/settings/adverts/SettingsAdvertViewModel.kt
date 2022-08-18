package tmg.flashback.ads.config.ui.settings.adverts

import dagger.hilt.android.lifecycle.HiltViewModel
import tmg.flashback.ads.config.R
import tmg.flashback.ads.config.repository.AdsRepository
import tmg.flashback.ui.settings.SettingsModel
import tmg.flashback.ui.settings.SettingsViewModel
import javax.inject.Inject

//region Inputs

interface SettingsAdvertViewModelInputs {

}

//endregion

//region Outputs

interface SettingsAdvertViewModelOutputs {

}

//endregion

@HiltViewModel
class SettingsAdvertViewModel @Inject constructor(
    private val adsRepository: AdsRepository
): SettingsViewModel(), SettingsAdvertViewModelInputs, SettingsAdvertViewModelOutputs {

    override val models: List<SettingsModel> get() = mutableListOf<SettingsModel>().apply {
        add(SettingsModel.Header(R.string.settings_help_adverts_title))
        add(SettingsModel.SwitchPref(
            title = R.string.settings_help_adverts_title,
            description = R.string.settings_help_adverts_description,
            getState = { adsRepository.userPrefEnabled },
            saveState = { adsRepository.userPrefEnabled = it }
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
