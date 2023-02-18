package tmg.flashback.ui.settings.ads

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import tmg.flashback.ads.ads.repository.AdsRepository
import tmg.flashback.ui.settings.Setting
import tmg.flashback.ui.settings.Settings
import javax.inject.Inject

interface SettingsAdsViewModelInputs {
    fun prefClicked(pref: Setting)
}

interface SettingsAdsViewModelOutputs {
    val adsEnabled: LiveData<Boolean>
}

@HiltViewModel
class SettingsAdsViewModel @Inject constructor(
    private val adsRepository: AdsRepository
): ViewModel(), SettingsAdsViewModelInputs, SettingsAdsViewModelOutputs {

    val inputs: SettingsAdsViewModelInputs = this
    val outputs: SettingsAdsViewModelOutputs = this

    override val adsEnabled: MutableLiveData<Boolean> = MutableLiveData(adsRepository.userPrefEnabled)

    override fun prefClicked(pref: Setting) {
        when (pref.key) {
            Settings.Ads.enableAdsKey -> {
                adsRepository.userPrefEnabled = !adsRepository.userPrefEnabled
                adsEnabled.value = adsRepository.userPrefEnabled
            }
        }
    }
}