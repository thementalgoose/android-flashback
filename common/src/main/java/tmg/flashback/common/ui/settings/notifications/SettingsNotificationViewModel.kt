package tmg.flashback.common.ui.settings.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tmg.flashback.notifications.R
import tmg.flashback.notifications.controllers.NotificationController
import tmg.flashback.ui.settings.SettingsModel
import tmg.flashback.ui.settings.SettingsViewModel
import tmg.utilities.lifecycle.DataEvent
import tmg.utilities.lifecycle.Event

//region Inputs

interface SettingsNotificationViewModelInputs {

}

//endregion

//region Outputs

interface SettingsNotificationViewModelOutputs {
    val openNotificationsChannel: LiveData<DataEvent<String>>
    val openNotifications: LiveData<Event>
}

//endregion


class SettingsNotificationViewModel(
    private val notificationController: NotificationController
): SettingsViewModel(), SettingsNotificationViewModelInputs, SettingsNotificationViewModelOutputs {

    override val models: List<SettingsModel> = mutableListOf<SettingsModel>().apply {
        add(SettingsModel.Header(R.string.settings_notifications_title))
        if (notificationController.isNotificationChannelsSupported) {
            add(
                SettingsModel.Pref(
                title = R.string.settings_notifications_channel_qualifying_title,
                description = R.string.settings_notifications_channel_qualifying_description,
                onClick = {
                    openNotificationsChannel.value = DataEvent(keyNotificationChannelQualifying)
                }
            ))
            add(
                SettingsModel.Pref(
                title = R.string.settings_notifications_channel_race_title,
                description = R.string.settings_notifications_channel_race_description,
                onClick = {
                    openNotificationsChannel.value = DataEvent(keyNotificationChannelRace)
                }
            ))
        } else {
            add(
                SettingsModel.Pref(
                title = R.string.settings_notifications_nonchannel_title,
                description = R.string.settings_notifications_nonchannel_description,
                onClick = {
                    openNotifications.value = Event()
                }
            ))
        }
    }

    private val keyNotificationChannelQualifying: String = "qualifying"
    private val keyNotificationChannelRace: String = "race"

    var inputs: SettingsNotificationViewModelInputs = this
    var outputs: SettingsNotificationViewModelOutputs = this

    override val openNotifications: MutableLiveData<Event> = MutableLiveData()
    override val openNotificationsChannel: MutableLiveData<DataEvent<String>> = MutableLiveData()
}
