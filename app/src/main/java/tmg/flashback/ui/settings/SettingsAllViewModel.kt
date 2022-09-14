package tmg.flashback.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import tmg.flashback.ads.config.repository.AdsRepository
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.rss.repo.RSSRepository
import tmg.flashback.settings.SettingsNavigationComponent
import tmg.flashback.ui.repository.ThemeRepository
import javax.inject.Inject

interface SettingsAllViewModelInputs {
    fun itemClicked(pref: Setting)
}

interface SettingsAllViewModelOutputs {
    val isThemeEnabled: LiveData<Boolean>
    val isAdsEnabled: LiveData<Boolean>
    val isRSSEnabled: LiveData<Boolean>
}

@HiltViewModel
class SettingsAllViewModel @Inject constructor(
    themeRepository: ThemeRepository,
    buildConfig: BuildConfigManager,
    adsRepository: AdsRepository,
    rssRepository: RSSRepository,
    private val settingsNavigationComponent: SettingsNavigationComponent
): ViewModel(), SettingsAllViewModelInputs, SettingsAllViewModelOutputs {

    val inputs: SettingsAllViewModelInputs = this
    val outputs: SettingsAllViewModelOutputs = this

    override val isThemeEnabled: MutableLiveData<Boolean> = MutableLiveData()
    override val isAdsEnabled: MutableLiveData<Boolean> = MutableLiveData()
    override val isRSSEnabled: MutableLiveData<Boolean> = MutableLiveData()

    init {
        isThemeEnabled.value = themeRepository.enableThemePicker && buildConfig.isMonetThemeSupported
        isAdsEnabled.value = adsRepository.allowUserConfig
        isRSSEnabled.value = rssRepository.enabled
    }

    override fun itemClicked(pref: Setting) {
        when (pref.key) {
            AppSettings.Theme.darkMode.key -> {
                settingsNavigationComponent.nightModeDialog()
            }
            AppSettings.Theme.theme.key -> {
                settingsNavigationComponent.themeDialog()
            }
        }
    }
}