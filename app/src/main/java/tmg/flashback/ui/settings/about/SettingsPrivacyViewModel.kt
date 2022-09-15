package tmg.flashback.ui.settings.about

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import tmg.flashback.analytics.manager.AnalyticsManager
import tmg.flashback.crash_reporting.repository.CrashRepository
import tmg.flashback.settings.SettingsNavigationComponent
import tmg.flashback.ui.settings.Settings
import tmg.flashback.ui.settings.Setting
import javax.inject.Inject

interface SettingsPrivacyViewModelInputs {
    fun prefClicked(pref: Setting)
}

interface SettingsPrivacyViewModelOutputs {
    val crashReportingEnabled: LiveData<Boolean>
    val analyticsEnabled: LiveData<Boolean>
}

@HiltViewModel
class SettingsPrivacyViewModel @Inject constructor(
    private val crashRepository: CrashRepository,
    private val analyticsManager: AnalyticsManager,
    private val settingsNavigationComponent: SettingsNavigationComponent
): ViewModel(), SettingsPrivacyViewModelInputs, SettingsPrivacyViewModelOutputs {

    val inputs: SettingsPrivacyViewModelInputs = this
    val outputs: SettingsPrivacyViewModelOutputs = this

    override val crashReportingEnabled: MutableLiveData<Boolean> = MutableLiveData(crashRepository.isEnabled)
    override val analyticsEnabled: MutableLiveData<Boolean> = MutableLiveData(analyticsManager.enabled)

    override fun prefClicked(pref: Setting) {
        when (pref.key) {
            Settings.Other.privacyPolicy.key -> {
                settingsNavigationComponent.privacyPolicy()
            }
            Settings.Other.crashReportingKey -> {
                crashRepository.isEnabled = !crashRepository.isEnabled
                crashReportingEnabled.value = crashRepository.isEnabled
            }
            Settings.Other.analyticsKey -> {
                analyticsManager.enabled = !analyticsManager.enabled
                analyticsEnabled.value = analyticsManager.enabled
            }
        }
    }

}