package tmg.flashback.ui.settings.about

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tmg.flashback.R
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.device.repository.PrivacyRepository
import tmg.flashback.navigation.ApplicationNavigationComponent
import tmg.flashback.ui.managers.ToastManager
import tmg.flashback.ui.settings.Setting
import tmg.flashback.ui.settings.Settings
import tmg.flashback.web.usecases.OpenWebpageUseCase
import javax.inject.Inject

interface SettingsAboutViewModelInputs {
    fun prefClicked(pref: Setting)
}

interface SettingsAboutViewModelOutputs {
    val shakeToReportEnabled: StateFlow<Boolean>
}

@HiltViewModel
class SettingsAboutViewModel @Inject constructor(
    private val privacyRepository: PrivacyRepository,
    private val applicationNavigationComponent: ApplicationNavigationComponent,
    private val openWebpageUseCase: OpenWebpageUseCase,
    private val toastManager: ToastManager,
    private val buildConfigManager: BuildConfigManager
): ViewModel(), SettingsAboutViewModelInputs, SettingsAboutViewModelOutputs {

    private val reviewUrl: String get() = "https://play.google.com/store/apps/details?id=${buildConfigManager.applicationId}"

    val inputs: SettingsAboutViewModelInputs = this
    val outputs: SettingsAboutViewModelOutputs = this

    override val shakeToReportEnabled: MutableStateFlow<Boolean> = MutableStateFlow(privacyRepository.shakeToReport)

    override fun prefClicked(pref: Setting) {
        when (pref.key) {
            Settings.Other.aboutThisApp.key -> {
                applicationNavigationComponent.aboutApp()
            }
            Settings.Other.review.key -> {
                openWebpageUseCase.open(url = reviewUrl, title = "")
            }
            Settings.Other.shakeToReportKey -> {
                privacyRepository.shakeToReport = !privacyRepository.shakeToReport
                shakeToReportEnabled.value = privacyRepository.shakeToReport
                toastManager.displayToast(R.string.settings_restart_app_required)
            }
        }
    }
}