package tmg.flashback.rss.ui.settings.configure

import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.tooling.preview.Preview
import tmg.flashback.rss.repo.model.SupportedArticleSource
import tmg.flashback.rss.ui.RSSItem
import tmg.flashback.rss.ui.settings.configure.layouts.*
import tmg.flashback.rss.ui.settings.configure.layouts.RssCustomLayout
import tmg.flashback.rss.ui.settings.configure.layouts.RssItemLayout
import tmg.flashback.rss.ui.settings.configure.layouts.RssQuickLinksLayout
import tmg.flashback.rss.ui.settings.configure.layouts.RssSectionLayout
import tmg.flashback.style.AppThemePreview

@Composable
fun RSSConfigureLayout(
    state: List<RSSConfigureItem>,
    removeItem: (link: String) -> Unit,
    addQuickLink: (supportedArticle: SupportedArticleSource) -> Unit,
    addCustom: (String) -> Unit,
    websiteClicked: (supportedArticle: SupportedArticleSource) -> Unit
) {
    LazyColumn {
        state.forEach { item ->
            when (item) {
                RSSConfigureItem.Add -> {
                    item(key = "ADD") {
                        RssCustomLayout(
                            addClicked = addCustom
                        )
                    }
                }
                is RSSConfigureItem.Header -> {
                    item(key = item.text) {
                        RssSectionLayout(
                            title = stringResource(id = item.text), 
                            subtitle = stringResource(id = item.subtitle)
                        )
                    }
                }
                is RSSConfigureItem.Item -> {
                    item(key = item.url) {
                        RssItemLayout(
                            model = item,
                            websiteClicked = websiteClicked,
                            specialClicked = removeItem
                        )
                    }
                }
                RSSConfigureItem.NoItems -> {
                    item(key = "NO_ITEMS") {
                        RssNoItemsLayout()
                    }
                }
                is RSSConfigureItem.QuickAdd -> {
                    item(key = item.supportedArticleSource.rssLink) {
                        RssQuickLinksLayout(
                            model = item,
                            websiteClicked = websiteClicked,
                            specialClicked = addQuickLink
                        )
                    }
                }
            }
        }
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