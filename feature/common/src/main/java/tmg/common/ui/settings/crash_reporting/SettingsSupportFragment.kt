package tmg.common.ui.settings.crash_reporting

import org.koin.android.ext.android.inject
import tmg.analytics.manager.AnalyticsManager
import tmg.common.R
import tmg.common.ui.settings.SettingsFragment
import tmg.common.ui.settings.SettingsModel
import tmg.crash_reporting.controllers.CrashController

class SettingsSupportFragment: SettingsFragment<SettingsSupportFragment>() {

    private val crashController: CrashController by inject()
    private val analyticsManager: AnalyticsManager by inject()

    override val models: List<SettingsModel<SettingsSupportFragment>> = listOf(
        SettingsModel.Header(
                    title = R.string.settings_device
        ),
        SettingsModel.SwitchPref(
            title = R.string.settings_help_crash_reporting_title,
            description = R.string.settings_help_crash_reporting_description,
            getState = { crashController.enabled },
            saveState = { enabled -> crashController.enabled = enabled }
        ),
        SettingsModel.SwitchPref(
            title = R.string.settings_help_analytics_title,
            description = R.string.settings_help_analytics_description,
            getState = { analyticsManager.enabled },
            saveState = { enabled -> analyticsManager.enabled = enabled }
        ),
        SettingsModel.SwitchPref(
            title = R.string.settings_help_shake_to_report_title,
            description = R.string.settings_help_shake_to_report_description,
            getState = { crashController.shakeToReport },
            saveState = { enabled -> crashController.shakeToReport = enabled }
        )
    )
}