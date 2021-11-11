package tmg.flashback.common.ui.settings.support

import tmg.flashback.common.R
import tmg.flashback.analytics.manager.AnalyticsManager
import tmg.flashback.crash_reporting.controllers.CrashController
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
    private val crashController: CrashController,
    private val analyticsManager: AnalyticsManager
): SettingsViewModel(), SettingsSupportViewModelInputs, SettingsSupportViewModelOutputs {

    override val models: List<SettingsModel> get() = listOf(
        SettingsModel.Header(R.string.settings_help),
        SettingsModel.SwitchPref(
            title = R.string.settings_help_crash_reporting_title,
            description = R.string.settings_help_crash_reporting_description,
            getState = { crashController.enabled },
            saveState = { crashController.enabled = it },
        ),
        SettingsModel.SwitchPref(
            title = R.string.settings_help_analytics_title,
            description = R.string.settings_help_analytics_description,
            getState = { analyticsManager.enabled },
            saveState = { analyticsManager.enabled = it },
        ),
        SettingsModel.SwitchPref(
            title = R.string.settings_help_shake_to_report_title,
            description = R.string.settings_help_shake_to_report_description,
            getState = { crashController.shakeToReport },
            saveState = { crashController.shakeToReport = it },
        )
    )

    var inputs: SettingsSupportViewModelInputs = this
    var outputs: SettingsSupportViewModelOutputs = this
}
