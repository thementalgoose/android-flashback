package tmg.flashback.ads.core.usecases

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import tmg.flashback.ads.contract.repository.AdsRepository
import tmg.flashback.ads.contract.usecases.InitialiseAdsUseCase
import tmg.flashback.ads.core.manager.AdsManager
import javax.inject.Inject

internal class InitialiseAdsUseCaseImpl @Inject constructor(
    @ApplicationContext
    private val applicationContext: Context,
    private val adsManager: AdsManager,
    private val adsRepository: AdsRepository
): InitialiseAdsUseCase {
    override fun initialise() {
        if (adsRepository.isEnabled) {
            adsManager.initialize(applicationContext)
        }
    }
}