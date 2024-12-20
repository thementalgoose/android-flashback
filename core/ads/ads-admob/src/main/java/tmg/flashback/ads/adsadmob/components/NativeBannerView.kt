package tmg.flashback.ads.adsadmob.components

import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.strings.R.string
import tmg.flashback.ads.adsadmob.views.NativeBanner
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.badge.Badge
import tmg.flashback.style.badge.BadgeView
import kotlin.math.roundToInt

val defaultAdIconSize = 32.dp
val defaultAdIconSpacingWidth = 8.dp

@Composable
internal fun NativeBannerView(
    horizontalPadding: Dp = 16.dp,
    adIconSize: Dp?,
    adIconSpacing: Dp?,
    badgeModifier: Modifier = Modifier.padding(
        start = horizontalPadding + (adIconSpacing ?: defaultAdIconSpacingWidth) + (adIconSize ?: defaultAdIconSize)
    ),
    adIndex: Int = 0,
) {
    val viewModel = hiltViewModel<NativeBannerViewModel>()

    NativeBannerView(
        showAdverts = viewModel.areAdvertsEnabled,
        horizontalPadding = horizontalPadding,
        adIconSpacing = adIconSpacing ?: defaultAdIconSpacingWidth,
        adIconSize = adIconSize ?: defaultAdIconSize,
        badgeModifier = badgeModifier,
        adIndex = adIndex
    )
}

@Composable
private fun NativeBannerView(
    showAdverts: Boolean,
    horizontalPadding: Dp,
    adIconSize: Dp,
    adIconSpacing: Dp,
    badgeModifier: Modifier = Modifier,
    adIndex: Int = 0,
) {
    if (showAdverts) {
        Column(modifier = Modifier) {
            AndroidView(factory = { context ->
                NativeBanner(context).apply {
                    this.setPadding(horizontalDp = horizontalPadding.value.roundToInt())
                    this.setAdAppIconSize(sizeDp = adIconSize.value.roundToInt())
                    this.setAdAppIconSpacingWidth(widthDp = adIconSpacing.value.roundToInt())
                    this.layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    this.adIndex = adIndex
                }
            })

            Row(modifier = badgeModifier) {
                BadgeView(
                    model = Badge(
                        label = stringResource(id = string.admob_advertisement)
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
        NativeBannerView(
            horizontalPadding = 16.dp,
            adIconSize = 32.dp,
            adIconSpacing = 8.dp
        )
    }
}