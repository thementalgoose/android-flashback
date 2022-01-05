package tmg.flashback.statistics.ui.settings.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tmg.flashback.statistics.BuildConfig
import tmg.flashback.statistics.R
import tmg.flashback.statistics.controllers.ScheduleController
import tmg.flashback.ui.settings.SettingsModel
import tmg.flashback.ui.settings.SettingsViewModel
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
    private val scheduleController: ScheduleController
): SettingsViewModel(), UpNextSettingsViewModelInputs, UpNextSettingsViewModelOutputs {

    var inputs: UpNextSettingsViewModelInputs = this
    var outputs: UpNextSettingsViewModelOutputs = this

    override val models: List<SettingsModel> get() = mutableListOf<SettingsModel>().apply {
        add(SettingsModel.Header(R.string.settings_up_next_category_title))
        add(SettingsModel.SwitchPref(
            title = R.string.settings_up_next_category_race_title,
            description = R.string.settings_up_next_category_race_descrition,
            getState = { scheduleController.notificationRace },
            saveState = {
                scheduleController.notificationRace = it
            }
        ))
        add(SettingsModel.SwitchPref(
            title = R.string.settings_up_next_category_qualifying_title,
            description = R.string.settings_up_next_category_qualifying_descrition,
            getState = { scheduleController.notificationQualifying },
            saveState = {
                scheduleController.notificationQualifying = it
            }
        ))
        add(SettingsModel.SwitchPref(
            title = R.string.settings_up_next_category_free_practice_title,
            description = R.string.settings_up_next_category_free_practice_descrition,
            getState = { scheduleController.notificationFreePractice },
            saveState = {
                scheduleController.notificationFreePractice = it
            }
        ))
        add(SettingsModel.SwitchPref(
            title = R.string.settings_up_next_category_other_title,
            description = R.string.settings_up_next_category_other_descrition,
            getState = { scheduleController.notificationSeasonInfo },
            saveState = {
                scheduleController.notificationSeasonInfo = it
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

        // TODO: Results available notifications - Remove
        if (BuildConfig.DEBUG) {
            add(SettingsModel.Header(R.string.settings_up_next_results_available_title))
            add(SettingsModel.SwitchPref(
                title = R.string.settings_up_next_results_race_title,
                description = R.string.settings_up_next_results_race_descrition,
                getState = { scheduleController.notificationRaceNotify },
                saveState = {
                    scheduleController.notificationRaceNotify = it
                }
            ))
            add(SettingsModel.SwitchPref(
                title = R.string.settings_up_next_results_qualifying_title,
                description = R.string.settings_up_next_results_qualifying_descrition,
                getState = { scheduleController.notificationQualifyingNotify },
                saveState = {
                    scheduleController.notificationQualifyingNotify = it
                }
            ))
        }
    }

    override val openTimePicker: MutableLiveData<Event> = MutableLiveData()

    //region Inputs

    //endregion

    //region Outputs

    //endregion
}
