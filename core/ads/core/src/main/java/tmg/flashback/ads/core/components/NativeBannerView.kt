package tmg.flashback.ads.core.components

import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.ads.core.R
import tmg.flashback.ads.core.views.NativeBanner
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.ui.components.badges.Badge
import tmg.flashback.ui.components.badges.BadgeView

@Composable
internal fun NativeBannerView(
    horizontalPadding: Boolean = false,
    badgeOffset: Boolean = false,
    adIndex: Int = 0,
) {
    val viewModel = hiltViewModel<NativeBannerViewModel>()

    NativeBannerView(
        showAdverts = viewModel.areAdvertsEnabled,
        horizontalPadding = horizontalPadding,
        badgeOffset = badgeOffset,
        adIndex = adIndex
    )
}

@Composable
private fun NativeBannerView(
    showAdverts: Boolean,
    horizontalPadding: Boolean = false,
    badgeOffset: Boolean = false,
    adIndex: Int = 0,
) {
    if (showAdverts) {
        Column(modifier = Modifier) {
            AndroidView(factory = { context ->
                NativeBanner(context).apply {
                    if (horizontalPadding) {
                        this.setPadding(horizontalDp = 16)
                    }
                    this.layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    this.adIndex = adIndex
                    this.offsetCard = !badgeOffset
                }
            })

            val admobIconImageSize = 36.dp
            val startPadding = when {
                horizontalPadding && badgeOffset -> 16.dp + admobIconImageSize + 16.dp
                horizontalPadding -> 16.dp
                badgeOffset -> 16.dp + admobIconImageSize
                else -> 0.dp
            }

            Row(modifier = Modifier.padding(start = startPadding)) {
                BadgeView(
                    model = Badge(
                        label = R.string.admob_advertisement
                    )
                )
            }
        }
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        NativeBannerView()
    }
}