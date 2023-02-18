package tmg.flashback.ads.noop.usecases

import tmg.flashback.ads.contract.usecases.InitialiseAdsUseCase
import javax.inject.Inject

internal class NoopInitialiseAdsUseCaseImpl @Inject constructor(): InitialiseAdsUseCase {
    override fun initialise() {
        /* Noop */
    }
}