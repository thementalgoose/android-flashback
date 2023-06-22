package tmg.flashback.ui.settings.about

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tmg.flashback.R
import tmg.flashback.analytics.manager.AnalyticsManager
import tmg.flashback.device.repository.PrivacyRepository
import tmg.flashback.navigation.Navigator
import tmg.flashback.privacypolicy.contract.PrivacyPolicy
import tmg.flashback.ui.managers.ToastManager
import tmg.flashback.ui.settings.Setting
import tmg.flashback.ui.settings.Settings
import javax.inject.Inject

interface SettingsPrivacyViewModelInputs {
    fun prefClicked(pref: Setting)
}

interface SettingsPrivacyViewModelOutputs {
    val crashReportingEnabled: StateFlow<Boolean>
    val analyticsEnabled: StateFlow<Boolean>
}

@HiltViewModel
class SettingsPrivacyViewModel @Inject constructor(
    private val privacyRepository: PrivacyRepository,
    private val toastManager: ToastManager,
    private val navigator: Navigator,
): ViewModel(), SettingsPrivacyViewModelInputs, SettingsPrivacyViewModelOutputs {

    val inputs: SettingsPrivacyViewModelInputs = this
    val outputs: SettingsPrivacyViewModelOutputs = this

    override val crashReportingEnabled: MutableStateFlow<Boolean> = MutableStateFlow(privacyRepository.crashReporting)
    override val analyticsEnabled: MutableStateFlow<Boolean> = MutableStateFlow(privacyRepository.analytics)

    override fun prefClicked(pref: Setting) {
        when (pref.key) {
            Settings.Other.privacyPolicy.key -> {
                navigator.navigate(tmg.flashback.navigation.Screen.Settings.PrivacyPolicy)
            }
            Settings.Other.crashReportingKey -> {
                privacyRepository.crashReporting = !privacyRepository.crashReporting
                crashReportingEnabled.value = privacyRepository.crashReporting
                toastManager.displayToast(R.string.settings_restart_app_required)
            }
            Settings.Other.analyticsKey -> {
                privacyRepository.analytics = !privacyRepository.analytics
                analyticsEnabled.value = privacyRepository.analytics
                toastManager.displayToast(R.string.settings_restart_app_required)
            }
        }
    }

}