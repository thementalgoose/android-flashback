package tmg.flashback.ads.core.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import tmg.flashback.ads.contract.components.NativeBannerProvider
import javax.inject.Inject

internal class NativeBannerProviderImpl @Inject constructor(): NativeBannerProvider {

    @Composable
    override fun NativeBannerImpl(
        modifier: Modifier,
        badgeOffset: Boolean,
        adIndex: Int
    ) {
        NativeBanner(
            modifier = modifier,
            badgeOffset = badgeOffset,
            adIndex = adIndex
        )
    }
}