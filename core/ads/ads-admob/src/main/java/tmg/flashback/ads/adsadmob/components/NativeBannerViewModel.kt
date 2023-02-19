package tmg.flashback.ads.adsadmob.components

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import tmg.flashback.ads.ads.repository.AdsRepository
import javax.inject.Inject

@HiltViewModel
internal class NativeBannerViewModel @Inject constructor(
    private val adsRepository: AdsRepository
): ViewModel() {
    val areAdvertsEnabled: Boolean
        get() = adsRepository.areAdvertsEnabled
}