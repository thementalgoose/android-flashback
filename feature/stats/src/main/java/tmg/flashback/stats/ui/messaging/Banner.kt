package tmg.flashback.stats.ui.messaging

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.inject
import tmg.flashback.stats.R
import tmg.flashback.stats.repository.HomeRepository
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.ui.components.layouts.Container
import tmg.flashback.ui.navigation.ApplicationNavigationComponent

@Composable
fun Banner(
    modifier: Modifier = Modifier
) {
    val homeRepository: HomeRepository by inject()
    val banner = homeRepository.banner ?: return

    val navigationComponent: ApplicationNavigationComponent by inject()

    if (banner.url != null) {
        Banner(
            message = banner.message,
            showLink = true,
            modifier = modifier.clickable(onClick = {
                navigationComponent.openUrl(banner.url)
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
private fun Banner(
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
                vertical = AppTheme.dimensions.paddingXSmall,
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

@Preview
@Composable
private fun PreviewLight() {
    AppThemePreview(isLight = true) {
        Banner(
            message = "We stand with Ukraine",
            showLink = false
        )
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppThemePreview(isLight = false) {
        Banner(
            message = "We stand with Ukraine",
            showLink = true
        )
    }
}