package tmg.flashback.ads.core.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import tmg.flashback.ads.contract.components.AdvertProvider
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