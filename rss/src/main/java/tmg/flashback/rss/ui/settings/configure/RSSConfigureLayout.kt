package tmg.flashback.rss.ui.settings.configure

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import tmg.flashback.rss.R
import tmg.flashback.ui.layout.Screen
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import tmg.flashback.rss.repo.model.SupportedArticleSource
import tmg.flashback.rss.ui.RSSItem
import tmg.flashback.rss.ui.settings.configure.layouts.*
import tmg.flashback.rss.ui.settings.configure.layouts.RssCustomLayout
import tmg.flashback.rss.ui.settings.configure.layouts.RssItemLayout
import tmg.flashback.rss.ui.settings.configure.layouts.RssQuickLinksLayout
import tmg.flashback.rss.ui.settings.configure.layouts.RssSectionLayout
import tmg.flashback.style.AppThemePreview

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RSSConfigureLayout(
    state: List<RSSConfigureItem>,
    removeItem: (link: String) -> Unit,
    addQuickLink: (supportedArticle: SupportedArticleSource) -> Unit,
    addCustom: (String) -> Unit,
    websiteClicked: (supportedArticle: SupportedArticleSource) -> Unit
) {
    LazyColumn {
        items(state,
            key = {
                when (it) {
                    RSSConfigureItem.Add -> "ADD"
                    is RSSConfigureItem.Header -> it.text
                    is RSSConfigureItem.Item -> it.url
                    RSSConfigureItem.NoItems -> "NO_ITEMS"
                    is RSSConfigureItem.QuickAdd -> it.supportedArticleSource.rssLink
                }
            },
            itemContent = {
                when (it) {
                    RSSConfigureItem.Add -> {
                        RssCustomLayout(
                            addClicked = addCustom,
                            modifier = Modifier.animateItemPlacement()
                        )
                    }
                    is RSSConfigureItem.Header -> {
                        RssSectionLayout(
                            title = stringResource(id = it.text),
                            subtitle = stringResource(id = it.subtitle),
                            modifier = Modifier.animateItemPlacement()
                        )
                    }
                    is RSSConfigureItem.Item -> {
                        RssItemLayout(
                            model = it,
                            modifier = Modifier.animateItemPlacement(),
                            websiteClicked = websiteClicked,
                            specialClicked = removeItem
                        )
                    }
                    RSSConfigureItem.NoItems -> {
                        RssNoItemsLayout(
                            modifier = Modifier.animateItemPlacement()
                        )
                    }
                    is RSSConfigureItem.QuickAdd -> {
                        RssQuickLinksLayout(
                            model = it,
                            modifier = Modifier.animateItemPlacement(),
                            websiteClicked = websiteClicked,
                            specialClicked = addQuickLink
                        )
                    }
                }
            }
        )
    }
}

@Preview
@Composable
private fun PreviewLight() {
    AppThemePreview {
        RSSConfigureLayout(
            state = listOf(
                RSSConfigureItem.Header(
                    text = R.string.rss_configure_header_items,
                    subtitle = R.string.rss_configure_header_add_subtitle
                ),
                RSSConfigureItem.Item(
                    url = "https://www.google.com",
                    supportedArticleSource = null
                ),
                RSSConfigureItem.Header(
                    text = R.string.rss_configure_header_quick_add,
                    subtitle = R.string.rss_configure_header_quick_add_subtitle
                ),
                RSSConfigureItem.QuickAdd(
                    supportedArticleSource = SupportedArticleSource(
                        rssLink = "https://formula1.com/link/to/rss",
                        sourceShort = "F1",
                        source = "https://formula1.com",
                        colour = "#e83040",
                        textColour = "#f8f8f8",
                        title = "HELP",
                        contactLink = "https://formula1.com/contact"
                    )
                ),
                RSSConfigureItem.Header(
                    text = R.string.rss_configure_header_add,
                    subtitle = R.string.rss_configure_header_add_subtitle
                ),
                RSSConfigureItem.Add
            ),
            removeItem = { },
            addQuickLink = { },
            addCustom = { },
            websiteClicked = { }
        )
    }
}