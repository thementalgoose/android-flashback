package tmg.flashback.upnext.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tmg.flashback.ui.settings.SettingsModel
import tmg.flashback.ui.settings.SettingsViewModel
import tmg.flashback.upnext.R
import tmg.flashback.upnext.controllers.UpNextController
import tmg.utilities.lifecycle.Event

//region Inputs

interface UpNextSettingsViewModelInputs {

}

//endregion

//region Outputs

interface UpNextSettingsViewModelOutputs {
    val openTimePicker: LiveData<Event>
}

//endregion

class UpNextSettingsViewModel(
    private val upNextController: UpNextController
): SettingsViewModel(), UpNextSettingsViewModelInputs, UpNextSettingsViewModelOutputs {

    var inputs: UpNextSettingsViewModelInputs = this
    var outputs: UpNextSettingsViewModelOutputs = this

    override val models: List<SettingsModel> get() = mutableListOf<SettingsModel>().apply {
        add(SettingsModel.Header(R.string.settings_up_next_category_title))
        add(SettingsModel.SwitchPref(
            title = R.string.settings_up_next_category_race_title,
            description = R.string.settings_up_next_category_race_descrition,
            getState = { upNextController.notificationRace },
            saveState = {
                upNextController.notificationRace = it
            }
        ))
        add(SettingsModel.SwitchPref(
            title = R.string.settings_up_next_category_qualifying_title,
            description = R.string.settings_up_next_category_qualifying_descrition,
            getState = { upNextController.notificationQualifying },
            saveState = {
                upNextController.notificationQualifying = it
            }
        ))
        add(SettingsModel.SwitchPref(
            title = R.string.settings_up_next_category_free_practice_title,
            description = R.string.settings_up_next_category_free_practice_descrition,
            getState = { upNextController.notificationFreePractice },
            saveState = {
                upNextController.notificationFreePractice = it
            }
        ))
        add(SettingsModel.SwitchPref(
            title = R.string.settings_up_next_category_other_title,
            description = R.string.settings_up_next_category_other_descrition,
            getState = { upNextController.notificationSeasonInfo },
            saveState = {
                upNextController.notificationSeasonInfo = it
            }
        ))
        add(SettingsModel.Header(R.string.settings_up_next_title))
        add(SettingsModel.Pref(
            title = R.string.settings_up_next_time_before_title,
            description = R.string.settings_up_next_time_before_description,
            onClick = {
                openTimePicker.value = Event()
            }
        ))
    }

    override val openTimePicker: MutableLiveData<Event> = MutableLiveData()

    //region Inputs

    //endregion

    //region Outputs

    //endregion
}
