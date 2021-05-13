package tmg.flashback.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tmg.core.ui.settings.SettingsModel
import tmg.core.ui.settings.SettingsViewModel
import tmg.flashback.R
import tmg.utilities.lifecycle.Event

//region Inputs

interface SettingsAllViewModelInputs {

}

//endregion

//region Outputs

interface SettingsAllViewModelOutputs {
    val openAbout: LiveData<Event>
}

//endregion


class SettingsAllViewModel: SettingsViewModel(), SettingsAllViewModelInputs, SettingsAllViewModelOutputs {

    override val models: List<SettingsModel> = listOf(
        SettingsModel.Header(R.string.settings_about),
        SettingsModel.Pref(
            title = R.string.settings_about,
            description = R.string.settings_about,
            onClick = {
                openAbout.value = Event()
            }
        )
    )

    override val openAbout: MutableLiveData<Event> = MutableLiveData()

    var inputs: SettingsAllViewModelInputs = this
    var outputs: SettingsAllViewModelOutputs = this

    init {

    }

    //region Inputs

    //endregion

    //region Outputs

    //endregion
}
