package tmg.flashback.ui.settings.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tmg.components.prefs.AppPreferencesItem
import tmg.flashback.R
import tmg.flashback.controllers.NotificationController
import tmg.flashback.core.ui.BaseViewModel
import tmg.flashback.managers.notifications.FirebasePushNotificationManager.Companion.topicQualifying
import tmg.flashback.managers.notifications.FirebasePushNotificationManager.Companion.topicRace
import tmg.utilities.lifecycle.DataEvent
import tmg.utilities.lifecycle.Event

//region Inputs

interface SettingsNotificationViewModelInputs {
    fun preferenceClicked(pref: String?, value: Boolean?)
}

//endregion

//region Outputs

interface SettingsNotificationViewModelOutputs {
    val settings: LiveData<List<AppPreferencesItem>>

    val openNotificationsChannel: LiveData<DataEvent<String>>
    val openNotifications: LiveData<Event>
}

//endregion


class SettingsNotificationViewModel(
    private val notificationController: NotificationController
): BaseViewModel(), SettingsNotificationViewModelInputs, SettingsNotificationViewModelOutputs {

    private val keyNotificationChannelQualifying: String = "NotificationQualifying"
    private val keyNotificationChannelRace: String = "NotificationRace"
    private val keyNotificationSettings: String = "NotificationSettings"

    var inputs: SettingsNotificationViewModelInputs = this
    var outputs: SettingsNotificationViewModelOutputs = this

    override val settings: MutableLiveData<List<AppPreferencesItem>> = MutableLiveData()

    override val openNotifications: MutableLiveData<Event> = MutableLiveData()
    override val openNotificationsChannel: MutableLiveData<DataEvent<String>> = MutableLiveData()

    init {
        settings.value = mutableListOf<AppPreferencesItem>().apply {
            add(AppPreferencesItem.Category(R.string.settings_notifications_title))
            if (notificationController.isNotificationChannelsSupported) {
                add(AppPreferencesItem.Preference(keyNotificationChannelQualifying, R.string.settings_notifications_channel_qualifying_title, R.string.settings_notifications_channel_qualifying_description))
                add(AppPreferencesItem.Preference(keyNotificationChannelRace, R.string.settings_notifications_channel_race_title, R.string.settings_notifications_channel_race_description))
            }
            else {
                add(AppPreferencesItem.Preference(keyNotificationSettings, R.string.settings_notifications_nonchannel_title, R.string.settings_notifications_nonchannel_description))
            }
        }
    }

    //region Inputs

    override fun preferenceClicked(pref: String?, value: Boolean?) {
        when (pref) {
            keyNotificationChannelQualifying -> openNotificationsChannel.value = DataEvent(topicQualifying)
            keyNotificationChannelRace -> openNotificationsChannel.value = DataEvent(topicRace)
            keyNotificationSettings -> openNotifications.value = Event()
        }
    }

    //endregion
}
