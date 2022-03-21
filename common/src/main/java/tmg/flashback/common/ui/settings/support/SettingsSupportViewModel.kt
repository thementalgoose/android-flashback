package tmg.flashback.common.ui.settings.support

import tmg.flashback.analytics.manager.AnalyticsManager
import tmg.flashback.common.R
import tmg.flashback.crash_reporting.repository.CrashRepository
import tmg.flashback.ui.settings.SettingsModel
import tmg.flashback.ui.settings.SettingsViewModel

//region Inputs

interface SettingsSupportViewModelInputs {

}

//endregion

//region Outputs

interface SettingsSupportViewModelOutputs {

}

//endregion

class SettingsSupportViewModel(
    private val crashRepository: CrashRepository,
    private val analyticsManager: AnalyticsManager
): SettingsViewModel(), SettingsSupportViewModelInputs, SettingsSupportViewModelOutputs {

    override val models: List<SettingsModel> get() = mutableListOf<SettingsModel>().apply {
        add(SettingsModel.Header(R.string.settings_help))
        add(SettingsModel.SwitchPref(
            title = R.string.settings_help_crash_reporting_title,
            description = R.string.settings_help_crash_reporting_description,
            getState = { crashRepository.isEnabled },
            saveState = { crashRepository.isEnabled = it },
        ))
        add(SettingsModel.SwitchPref(
            title = R.string.settings_help_analytics_title,
            description = R.string.settings_help_analytics_description,
            getState = { analyticsManager.enabled },
            saveState = { analyticsManager.enabled = it },
        ))
        add(SettingsModel.SwitchPref(
            title = R.string.settings_help_shake_to_report_title,
            description = R.string.settings_help_shake_to_report_description,
            getState = { crashRepository.shakeToReport },
            saveState = { crashRepository.shakeToReport = it },
        ))
    }

    var inputs: SettingsSupportViewModelInputs = this
    var outputs: SettingsSupportViewModelOutputs = this
}
