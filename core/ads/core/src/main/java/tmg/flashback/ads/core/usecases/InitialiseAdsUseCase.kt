package tmg.flashback.ads.core.usecases

import android.content.Context
import tmg.flashback.ads.contract.repository.AdsRepository
import tmg.flashback.ads.core.manager.AdsManager
import javax.inject.Inject

internal class InitialiseAdsUseCase @Inject constructor(
    private val applicationContext: Context,
    private val adsManager: AdsManager,
    private val adsRepository: AdsRepository
) {
    fun initialise() {
        if (adsRepository.isEnabled) {
            adsManager.initialize(applicationContext)
        }
    }
}