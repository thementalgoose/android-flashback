package tmg.flashback.rss.ui.settings.configure.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import tmg.flashback.rss.R
import tmg.flashback.rss.repo.model.SupportedArticleSource
import tmg.flashback.rss.ui.settings.configure.RSSConfigureItem
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.buttons.ButtonTertiary
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2

@Composable
internal fun RssQuickLinksLayout(
    model: RSSConfigureItem.QuickAdd,
    websiteClicked: (String) -> Unit,
    specialClicked: () -> Unit,
    isAdd: Boolean = true,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = modifier
            .defaultMinSize(0.dp, 0.dp)
            .padding(
                start = AppTheme.dimensions.paddingMedium,
                end = AppTheme.dimensions.paddingMedium,
                top = 10.dp,
                bottom = 10.dp
            )
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier
            .fillMaxHeight()
            .align(Alignment.CenterVertically)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .requiredWidth(32.dp)
                    .requiredHeight(32.dp)
                    .clip(CircleShape)
                    .background(Color(model.supportedArticleSource.colour.toColorInt()))
            ) {
                TextBody1(
                    text = model.supportedArticleSource.sourceShort,
                    textColor = Color.White
                )
            }
        }
        Column(modifier = Modifier
            .padding(
                start = AppTheme.dimensions.paddingSmall,
                end = AppTheme.dimensions.paddingSmall
            )
            .weight(1f)
        ) {
            TextBody1(
                text = model.supportedArticleSource.source,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )
            TextBody2(
                text = model.supportedArticleSource.rssLink,
                modifier = Modifier.fillMaxWidth()
            )
        }
        ButtonTertiary(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = stringResource(id = R.string.rss_configure_visit_website),
            onClick = {
                websiteClicked(model.supportedArticleSource.contactLink)
            }
        )
        Spacer(modifier = Modifier.width(AppTheme.dimensions.paddingSmall))
        Icon(
            modifier = Modifier
                .fillMaxHeight()
                .clickable(
                    onClick = specialClicked
                ),
            painter = painterResource(if (isAdd) R.drawable.ic_rss_configure_add else R.drawable.ic_rss_configure_remove),
            tint = if (isAdd) AppTheme.colors.rssAdd else AppTheme.colors.rssRemove,
            contentDescription = stringResource(id = R.string.ab_rss_configure_add)
        )
    }
}

@Preview
@Composable
private fun PreviewLight() {
    AppThemePreview(isLight = true) {
        RssQuickLinksLayout(
            model = RSSConfigureItem.QuickAdd(
                supportedArticleSource = SupportedArticleSource(
                    rssLink = "https://source.com/rss/feed/content.xml",
                    sourceShort = "RS",
                    source = "https://source.com",
                    colour = "#984332",
                    textColour = "#F8F8F8",
                    title = "Title",
                    contactLink = "https://contact.link"
                )
            ),
            websiteClicked = { },
            specialClicked = { }
        )
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppThemePreview(isLight = false) {
        RssQuickLinksLayout(
            model = RSSConfigureItem.QuickAdd(
                supportedArticleSource = SupportedArticleSource(
                    rssLink = "https://source.com/rss/feed/content.xml",
                    sourceShort = "RS",
                    source = "https://source.com",
                    colour = "#984332",
                    textColour = "#F8F8F8",
                    title = "Title",
                    contactLink = "https://contact.link"
                )
            ),
            websiteClicked = { },
            specialClicked = { },
            isAdd = false
        )
    }
}