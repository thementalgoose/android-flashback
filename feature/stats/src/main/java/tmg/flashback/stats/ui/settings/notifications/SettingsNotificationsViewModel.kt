package tmg.flashback.stats.ui.settings.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tmg.flashback.stats.R
import tmg.flashback.stats.StatsNavigationComponent
import tmg.flashback.stats.repository.NotificationRepository
import tmg.flashback.stats.repository.models.NotificationReminder
import tmg.flashback.stats.usecases.ResubscribeNotificationsUseCase
import tmg.flashback.ui.bottomsheet.BottomSheetItem
import tmg.flashback.ui.settings.SettingsModel
import tmg.flashback.ui.settings.SettingsViewModel
import tmg.utilities.lifecycle.Event
import tmg.utilities.models.Selected
import tmg.utilities.models.StringHolder

//region Inputs

interface SettingsNotificationViewModelInputs {

}

//endregion

//region Outputs

interface SettingsNotificationViewModelOutputs {
}

//endregion

class SettingsNotificationViewModel(
    private val notificationRepository: NotificationRepository,
    private val resubscribeNotificationsUseCase: ResubscribeNotificationsUseCase,
    private val statsNavigationComponent: StatsNavigationComponent
): SettingsViewModel(), SettingsNotificationViewModelInputs, SettingsNotificationViewModelOutputs {

    var inputs: SettingsNotificationViewModelInputs = this
    var outputs: SettingsNotificationViewModelOutputs = this

    override val models: List<SettingsModel> get() = mutableListOf<SettingsModel>().apply {
        add(SettingsModel.Header(R.string.settings_up_next_category_title))
        add(
            SettingsModel.SwitchPref(
            title = R.string.settings_up_next_category_race_title,
            description = R.string.settings_up_next_category_race_descrition,
            getState = { notificationRepository.notificationRace },
            saveState = {
                notificationRepository.notificationRace = it
            }
        ))
        add(
            SettingsModel.SwitchPref(
            title = R.string.settings_up_next_category_qualifying_title,
            description = R.string.settings_up_next_category_qualifying_descrition,
            getState = { notificationRepository.notificationQualifying },
            saveState = {
                notificationRepository.notificationQualifying = it
            }
        ))
        add(
            SettingsModel.SwitchPref(
            title = R.string.settings_up_next_category_free_practice_title,
            description = R.string.settings_up_next_category_free_practice_descrition,
            getState = { notificationRepository.notificationFreePractice },
            saveState = {
                notificationRepository.notificationFreePractice = it
            }
        ))
        add(
            SettingsModel.SwitchPref(
            title = R.string.settings_up_next_category_other_title,
            description = R.string.settings_up_next_category_other_descrition,
            getState = { notificationRepository.notificationOther },
            saveState = {
                notificationRepository.notificationOther = it
            }
        ))
        add(SettingsModel.Header(R.string.settings_up_next_title))
        add(
            SettingsModel.Pref(
            title = R.string.settings_up_next_time_before_title,
            description = R.string.settings_up_next_time_before_description,
            onClick = {
                statsNavigationComponent.upNext()
            }
        ))

        add(SettingsModel.Header(R.string.settings_up_next_results_available_title, beta = true))
        add(
            SettingsModel.SwitchPref(
            title = R.string.settings_up_next_results_race_title,
            description = R.string.settings_up_next_results_race_descrition,
            getState = { notificationRepository.notificationNotifyRace },
            saveState = {
                notificationRepository.notificationNotifyRace = it
                viewModelScope.launch {
                    resubscribeNotificationsUseCase.resubscribe()
                }
            }
        ))
        add(
            SettingsModel.SwitchPref(
            title = R.string.settings_up_next_results_sprint_title,
            description = R.string.settings_up_next_results_sprint_descrition,
            getState = { notificationRepository.notificationNotifySprint },
            saveState = {
                notificationRepository.notificationNotifySprint = it
                viewModelScope.launch {
                    resubscribeNotificationsUseCase.resubscribe()
                }
            }
        ))
        add(
            SettingsModel.SwitchPref(
            title = R.string.settings_up_next_results_qualifying_title,
            description = R.string.settings_up_next_results_qualifying_descrition,
            getState = { notificationRepository.notificationNotifyQualifying },
            saveState = {
                notificationRepository.notificationNotifyQualifying = it
                viewModelScope.launch {
                    resubscribeNotificationsUseCase.resubscribe()
                }
            }
        ))
    }

    //region Inputs

    //endregion

    //region Outputs

    //endregion
}
