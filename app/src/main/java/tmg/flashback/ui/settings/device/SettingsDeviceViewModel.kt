package tmg.flashback.ui.settings.device

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tmg.analytics.controllers.AnalyticsController
import tmg.components.prefs.AppPreferencesItem
import tmg.flashback.R
import tmg.flashback.core.controllers.CrashController
import tmg.flashback.core.controllers.DeviceController
import tmg.flashback.core.ui.BaseViewModel
import tmg.utilities.lifecycle.Event

//region Inputs

interface SettingsDeviceViewModelInputs {
    fun preferenceClicked(pref: String?, value: Boolean?)
}

//endregion

//region Outputs

interface SettingsDeviceViewModelOutputs {
    val settings: LiveData<List<AppPreferencesItem>>

    val openReleaseNotes: LiveData<Event>
}

//endregion

class SettingsDeviceViewModel(
    private val crashController: CrashController,
    private val analyticsController: AnalyticsController,
    private val deviceController: DeviceController
): BaseViewModel(), SettingsDeviceViewModelInputs, SettingsDeviceViewModelOutputs {

    private val keyCrash: String = "Crash"
    private val keyAnalytics: String = "Analytics"
    private val keyReleaseNotes: String = "ReleaseNotes"
    private val keyShake: String = "ShakeToReport"

    var inputs: SettingsDeviceViewModelInputs = this
    var outputs: SettingsDeviceViewModelOutputs = this

    override val settings: MutableLiveData<List<AppPreferencesItem>> = MutableLiveData()

    override val openReleaseNotes: MutableLiveData<Event> = MutableLiveData()

    init {
        settings.value = mutableListOf<AppPreferencesItem>().apply {
            add(AppPreferencesItem.Category(R.string.settings_device))
            add(AppPreferencesItem.Preference(
                prefKey = keyReleaseNotes,
                title = R.string.settings_help_release_notes_title,
                description = R.string.settings_help_release_notes_description
            ))
            add(AppPreferencesItem.SwitchPreference(keyCrash, R.string.settings_help_crash_reporting_title, R.string.settings_help_crash_reporting_description, crashController.enabled))
            add(AppPreferencesItem.SwitchPreference(keyAnalytics, R.string.settings_help_analytics_title, R.string.settings_help_analytics_description, analyticsController.enabled))
            add(AppPreferencesItem.SwitchPreference(keyShake, R.string.settings_help_shake_to_report_title, R.string.settings_help_shake_to_report_description, deviceController.shakeToReport))
        }
    }

    //region Inputs

    override fun preferenceClicked(pref: String?, value: Boolean?) {
        when (pref) {
            keyCrash -> crashController.enabled = value ?: true
            keyAnalytics -> analyticsController.enabled = value ?: true
            keyShake -> deviceController.shakeToReport = value ?: true
            keyReleaseNotes -> openReleaseNotes.value = Event()
        }
    }

    //endregion
}
