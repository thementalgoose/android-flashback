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
        val text: Int
    ): RSSConfigureItem(R.layout.view_rss_configure_header)

    data class Item(
        val url: String,
        val supportedArticleSource: SupportedArticleSource?
    ): RSSConfigureItem(R.layout.view_rss_configure_item)

    data class QuickAdd(
        val supportedArticleSource: SupportedArticleSource
    ): RSSConfigureItem(R.layout.view_rss_configure_quickadd)
}