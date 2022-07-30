package tmg.flashback.stats.ui.messaging

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.stats.R
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody2


@Composable
fun Banner(
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<BannerViewModel>()
    val banner = viewModel.message ?: return

    if (banner.url != null) {
        Banner(
            message = banner.message,
            showLink = true,
            modifier = modifier.clickable(onClick = {
                viewModel.navigateToWeb(banner.url)
            })
        )
    } else {
        Banner(
            message = banner.message,
            showLink = false
        )
    }
}

@Composable
fun Banner(
    message: String,
    showLink: Boolean,
    modifier: Modifier = Modifier
) {
//    Container(
//        isOutlined = true,
//        modifier = modifier
//            .padding(
//                vertical = AppTheme.dimensions.paddingXSmall,
//                horizontal = AppTheme.dimensions.paddingMedium
//            )
//            .fillMaxWidth()
//    ) {
        Row(modifier = Modifier
            .padding(
                vertical = AppTheme.dimensions.paddingSmall,
                horizontal = AppTheme.dimensions.paddingMedium
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextBody2(
                text = message,
                bold = true,
                modifier = Modifier.weight(1f)
            )
            if (showLink) {
                Spacer(Modifier.width(AppTheme.dimensions.paddingMedium))
                Icon(
                    modifier = Modifier.size(16.dp),
                    painter = painterResource(id = R.drawable.ic_banner_link),
                    tint = AppTheme.colors.contentTertiary,
                    contentDescription = null
                )
            }
        }
//    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        Banner(
            message = "We stand with Ukraine",
            showLink = false
        )
    }
}