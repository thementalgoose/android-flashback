package tmg.flashback.ads.adsadmob.components

import androidx.compose.runtime.Composable
import tmg.flashback.ads.ads.components.AdvertProvider
import javax.inject.Inject

internal class NativeBannerProviderImpl @Inject constructor(): AdvertProvider {

    @Composable
    override fun NativeBannerImpl(
        horizontalPadding: Boolean,
        badgeOffset: Boolean,
        adIndex: Int
    ) {
        NativeBannerView(
            horizontalPadding = horizontalPadding,
            badgeOffset = badgeOffset,
            adIndex = adIndex
        )
    }
}