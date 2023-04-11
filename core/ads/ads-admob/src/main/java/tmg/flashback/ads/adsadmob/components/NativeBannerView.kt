package tmg.flashback.ads.adsadmob.components

import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.ads.adsadmob.R
import tmg.flashback.ads.adsadmob.views.NativeBanner
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.badge.Badge
import tmg.flashback.style.badge.BadgeView

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
                        label = stringResource(id = R.string.admob_advertisement)
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