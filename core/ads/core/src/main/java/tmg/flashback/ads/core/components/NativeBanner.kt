package tmg.flashback.ads.core.components

import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
internal fun NativeBanner(
    modifier: Modifier = Modifier,
    badgeOffset: Boolean = false,
    adIndex: Int = 0,
) {
    val viewModel = hiltViewModel<NativeBannerViewModel>()

    NativeBanner(
        showAdverts = viewModel.areAdvertsEnabled,
        modifier = modifier,
        badgeOffset = badgeOffset,
        adIndex = adIndex
    )
}

@Composable
private fun NativeBanner(
    showAdverts: Boolean,
    modifier: Modifier = Modifier,
    badgeOffset: Boolean = false,
    adIndex: Int = 0,
) {
    if (showAdverts) {
        Column(
            modifier = modifier
                .padding(horizontal = AppTheme.dimens.medium)
        ) {
            AndroidView(factory = { context ->
                NativeBanner(context).apply {
                    this.layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    this.adIndex = adIndex
                    this.offsetCard = !badgeOffset
                }
            })
            Row(modifier = Modifier.padding(start = if (badgeOffset) 58.dp else 0.dp)) {
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
        NativeBanner()
    }
}