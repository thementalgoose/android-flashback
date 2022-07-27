package tmg.flashback.ads.components

import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import org.koin.androidx.compose.inject
import tmg.flashback.ads.R
import tmg.flashback.ads.config.repository.AdsRepository
import tmg.flashback.ads.views.NativeBanner
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.ui.components.badges.Badge
import tmg.flashback.ui.components.badges.BadgeView

@Composable
fun NativeBanner(
    modifier: Modifier = Modifier,
    badgeOffset: Boolean = false,
    adIndex: Int = 0,
) {
    val advertsRepository: AdsRepository by inject()

    NativeBanner(
        showAdverts = advertsRepository.areAdvertsEnabled,
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
                .padding(horizontal = AppTheme.dimensions.paddingMedium)
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