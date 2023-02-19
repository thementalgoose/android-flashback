package tmg.flashback.ads.adsadmob.usecases

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import tmg.flashback.ads.ads.repository.AdsRepository
import tmg.flashback.ads.ads.usecases.InitialiseAdsUseCase
import tmg.flashback.ads.adsadmob.manager.AdsManager
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