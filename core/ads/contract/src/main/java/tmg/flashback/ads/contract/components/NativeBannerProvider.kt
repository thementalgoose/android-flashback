package tmg.flashback.ads.contract.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface NativeBannerProvider {

    @Composable
    fun NativeBannerImpl(
        modifier: Modifier,
        badgeOffset: Boolean,
        adIndex: Int
    )

    @Composable
    fun NativeBanner(
        modifier: Modifier = Modifier,
        badgeOffset: Boolean = false,
        adIndex: Int = 0
    ) {
        NativeBanner(
            modifier = modifier,
            badgeOffset = badgeOffset,
            adIndex = 0
        )
    }
}