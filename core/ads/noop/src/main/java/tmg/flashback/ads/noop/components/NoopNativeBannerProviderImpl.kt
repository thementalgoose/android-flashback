package tmg.flashback.ads.noop.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import tmg.flashback.ads.contract.components.NativeBannerProvider
import javax.inject.Inject

class NoopNativeBannerProviderImpl @Inject constructor(): NativeBannerProvider {

    @Composable
    override fun NativeBannerImpl(
        modifier: Modifier,
        badgeOffset: Boolean,
        adIndex: Int
    ) {
        /* No-op */
    }
}