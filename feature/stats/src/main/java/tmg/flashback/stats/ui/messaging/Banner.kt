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
import tmg.flashback.stats.repository.models.Banner
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody2
import tmg.flashback.ui.components.layouts.Container

@Composable
fun Banner(
    modifier: Modifier = Modifier,
    season: Int? = null
) {
    val viewModel = hiltViewModel<BannerViewModel>()
    val bannerList = viewModel.message

    Banners(
        modifier = modifier,
        list = bannerList.filter { it.season == season },
        navigateToWeb = viewModel.inputs::navigateToWeb
    )
}

@Composable
private fun Banners(
    modifier: Modifier = Modifier,
    list: List<Banner>,
    navigateToWeb: (String) -> Unit,
) {
    Column(modifier) {
        list.forEach { banner ->
            if (banner.url != null) {
                Banner(
                    message = banner.message,
                    showLink = true,
                    highlight = banner.highlight,
                    modifier = modifier.clickable(onClick = {
                        navigateToWeb(banner.url)
                    })
                )
            } else {
                Banner(
                    message = banner.message,
                    highlight = banner.highlight,
                    showLink = false
                )
            }
        }
    }
}

@Composable
fun Banner(
    message: String,
    showLink: Boolean,
    modifier: Modifier = Modifier,
    highlight: Boolean = false,
) {
    Box(modifier = modifier.padding(
        horizontal = AppTheme.dimens.small,
        vertical = AppTheme.dimens.xsmall
    )) {
        Container(
            isOutlined = highlight
        ) {
            Row(Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = AppTheme.dimens.small,
                    vertical = AppTheme.dimens.xsmall
                )
            ) {
                TextBody2(
                    text = message,
                    bold = true,
                    modifier = Modifier.weight(1f)
                )
                if (showLink) {
                    Spacer(Modifier.width(AppTheme.dimens.medium))
                    Icon(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(id = R.drawable.ic_banner_link),
                        tint = AppTheme.colors.contentTertiary,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        Banners(
            list = listOf(
                Banner(
                    message = "Message 1",
                    url = "https://www.google.com",
                    highlight = false,
                    season = 0
                ),
                Banner(
                    message = "Message 2",
                    url = "https://www.google.com",
                    highlight = true,
                    season = 0
                ),
                Banner(
                    message = "Message 3",
                    url = null,
                    highlight = false,
                    season = 0
                ),
                Banner(
                    message = "Message 4",
                    url = null,
                    highlight = true,
                    season = 0
                )
            ),
            navigateToWeb = { }
        )
    }
}