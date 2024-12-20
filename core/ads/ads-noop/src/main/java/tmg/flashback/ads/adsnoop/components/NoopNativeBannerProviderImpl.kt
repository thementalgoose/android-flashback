package tmg.flashback.ads.adsnoop.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tmg.flashback.ads.ads.components.AdvertProvider
import javax.inject.Inject

class NoopNativeBannerProviderImpl @Inject constructor(): AdvertProvider {

    @Composable
    override fun NativeBannerImpl(
        horizontalPadding: Dp,
        adIconSize: Dp?,
        adIconSpacing: Dp?,
        adIndex: Int
    ) {
        Box(Modifier.size(0.dp))
    }
}