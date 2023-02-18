package tmg.flashback.ads.contract.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tmg.flashback.style.text.TextBody2

interface AdvertProvider {

    @Composable
    fun NativeBannerImpl(
        horizontalPadding: Boolean,
        badgeOffset: Boolean,
        adIndex: Int
    )

    @Composable
    fun NativeBanner(
        horizontalPadding: Boolean = false,
        badgeOffset: Boolean = true,
        adIndex: Int = 0
    ) {
        NativeBannerImpl(
            horizontalPadding = horizontalPadding,
            badgeOffset = badgeOffset,
            adIndex = 0
        )
    }
}

val fakeAdvertProvider = object : AdvertProvider {
    @Composable
    override fun NativeBannerImpl(
        horizontalPadding: Boolean,
        badgeOffset: Boolean,
        adIndex: Int
    ) {
        Box(Modifier.padding(16.dp)) {
            TextBody2(text = "Advert")
        }
    }

}