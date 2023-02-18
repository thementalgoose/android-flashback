package tmg.flashback.ads.core.components

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import tmg.flashback.ads.contract.repository.AdsRepository
import javax.inject.Inject

@HiltViewModel
internal class NativeBannerViewModel @Inject constructor(
    private val adsRepository: AdsRepository
): ViewModel() {
    val areAdvertsEnabled: Boolean
        get() = adsRepository.areAdvertsEnabled
}