package tmg.flashback.ads.adsnoop.usecases

import tmg.flashback.ads.ads.usecases.InitialiseAdsUseCase
import javax.inject.Inject

internal class NoopInitialiseAdsUseCaseImpl @Inject constructor(): InitialiseAdsUseCase {
    override fun initialise() {
        /* Noop */
    }
}