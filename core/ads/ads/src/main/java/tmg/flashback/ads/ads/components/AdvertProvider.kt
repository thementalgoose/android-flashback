package tmg.flashback.ads.ads.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tmg.flashback.style.text.TextBody2

interface AdvertProvider {

    @Composable
    fun NativeBannerImpl(
        horizontalPadding: Dp,
        adIconSize: Dp?,
        adIconSpacing: Dp?,
        adIndex: Int
    )

    // Default params not working with abstract compose functions,
    //  so null = use default value
    @Composable
    fun NativeBanner(
        horizontalPadding: Dp,
        adIconSize: Dp?,
        adIconSpacing: Dp?,
        adIndex: Int
    ) {
        NativeBannerImpl(
            horizontalPadding = horizontalPadding,
            adIconSize = adIconSize,
            adIconSpacing = adIconSpacing,
            adIndex = adIndex
        )
    }
}

val fakeAdvertProvider = object : AdvertProvider {
    @Composable
    override fun NativeBannerImpl(
        horizontalPadding: Dp,
        adIconSize: Dp?,
        adIconSpacing: Dp?,
        adIndex: Int
    ) {
        Box(Modifier.padding(horizontalPadding)) {
            TextBody2(text = "Advert")
        }
    }
}