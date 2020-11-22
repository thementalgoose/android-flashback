package tmg.flashback.rss.configure

import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import tmg.flashback.R
import tmg.flashback.repo.enums.SupportedArticleSource

sealed class RSSConfigureItem(
    @LayoutRes val layoutId: Int
) {
    data class Header(
        @StringRes
        val text: Int,
        @StringRes
        val subtitle: Int? = null
    ): RSSConfigureItem(R.layout.view_rss_configure_header)

    object NoItems: RSSConfigureItem(R.layout.view_rss_configure_no_items)

    data class Item(
        val url: String,
        val supportedArticleSource: SupportedArticleSource?
    ): RSSConfigureItem(R.layout.view_rss_configure_item)

    data class QuickAdd(
        val supportedArticleSource: SupportedArticleSource
    ): RSSConfigureItem(R.layout.view_rss_configure_quickadd)
}