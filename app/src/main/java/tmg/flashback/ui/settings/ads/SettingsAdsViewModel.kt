package tmg.flashback.ui.settings.ads

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tmg.flashback.ads.ads.repository.AdsRepository
import tmg.flashback.ui.settings.Setting
import tmg.flashback.ui.settings.Settings
import javax.inject.Inject

interface SettingsAdsViewModelInputs {
    fun prefClicked(pref: Setting)
}

interface SettingsAdsViewModelOutputs {
    val adsEnabled: StateFlow<Boolean>
}

@HiltViewModel
class SettingsAdsViewModel @Inject constructor(
    private val adsRepository: AdsRepository
): ViewModel(), SettingsAdsViewModelInputs, SettingsAdsViewModelOutputs {

    val inputs: SettingsAdsViewModelInputs = this
    val outputs: SettingsAdsViewModelOutputs = this

    override val adsEnabled: MutableStateFlow<Boolean> = MutableStateFlow(adsRepository.userPrefEnabled)

    override fun prefClicked(pref: Setting) {
        when (pref.key) {
            Settings.Ads.enableAds -> {
                adsRepository.userPrefEnabled = !adsRepository.userPrefEnabled
                adsEnabled.value = adsRepository.userPrefEnabled
            }
        }
    }
}