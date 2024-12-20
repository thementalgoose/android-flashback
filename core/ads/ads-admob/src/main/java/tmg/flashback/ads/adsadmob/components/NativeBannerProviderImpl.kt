package tmg.flashback.ads.adsadmob.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tmg.flashback.ads.ads.components.AdvertProvider
import javax.inject.Inject

internal class NativeBannerProviderImpl @Inject constructor(): AdvertProvider {

    @Composable
    override fun NativeBannerImpl(
        horizontalPadding: Dp,
        adIconSize: Dp?,
        adIconSpacing: Dp?,
        adIndex: Int
    ) {
        NativeBannerView(
            horizontalPadding = horizontalPadding,
            adIconSize = adIconSize,
            adIconSpacing = adIconSpacing,
            adIndex = adIndex
        )
    }
}