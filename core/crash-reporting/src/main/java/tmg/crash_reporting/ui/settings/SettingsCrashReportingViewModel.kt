package tmg.crash_reporting.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tmg.components.prefs.AppPreferencesItem
import tmg.crash_reporting.R
import tmg.crash_reporting.controllers.CrashController
import tmg.utilities.lifecycle.Event

//region Inputs

interface SettingsCrashReportingViewModelInputs {
    fun preferenceClicked(pref: String?, value: Boolean?)
}

//endregion

//region Outputs

interface SettingsCrashReportingViewModelOutputs {
    val settings: LiveData<List<AppPreferencesItem>>

    val notifyPreferencesAppliedAfterRestart: LiveData<Event>
}

//endregion

class SettingsCrashReportingViewModel(
    private val crashController: CrashController
): ViewModel(), SettingsCrashReportingViewModelInputs, SettingsCrashReportingViewModelOutputs {

    private val keyCrashReporting: String = "CrashReporting"
    private val keyShakeToReport: String = "ShakeToReport"

    var inputs: SettingsCrashReportingViewModelInputs = this
    var outputs: SettingsCrashReportingViewModelOutputs = this

    override val settings: MutableLiveData<List<AppPreferencesItem>> = MutableLiveData()
    override val notifyPreferencesAppliedAfterRestart: MutableLiveData<Event> = MutableLiveData()

    init {
        settings.value = listOf(
            AppPreferencesItem.Category(R.string.settings_crash_reporting_title),
            AppPreferencesItem.SwitchPreference(keyCrashReporting, R.string.settings_crash_reporting_enabled_title, R.string.settings_crash_reporting_enabled_description, crashController.enabled),
            AppPreferencesItem.SwitchPreference(keyShakeToReport, R.string.settings_crash_reporting_shake_to_report_title, R.string.settings_crash_reporting_shake_to_report_description, crashController.shakeToReport)
        )
    }

    //region Inputs

    override fun preferenceClicked(pref: String?, value: Boolean?) {
        when (pref) {
            keyCrashReporting -> {
                crashController.enabled = value ?: true
                notifyPreferencesAppliedAfterRestart.value = Event()
            }
            keyShakeToReport -> {
                crashController.shakeToReport = value ?: true
                notifyPreferencesAppliedAfterRestart.value = Event()
            }
        }
    }

    //endregion

}
