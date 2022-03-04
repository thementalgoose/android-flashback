package tmg.flashback.ads.usecases

import android.content.Context
import tmg.flashback.ads.manager.AdsManager
import tmg.flashback.ads.repository.AdsRepository

class InitialiseAdsUseCase(
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