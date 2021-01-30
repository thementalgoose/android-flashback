package tmg.flashback.rss.ui.configure

import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import tmg.flashback.rss.R
import tmg.flashback.rss.repo.enums.SupportedArticleSource

sealed class RSSConfigureItem(
    @LayoutRes val layoutId: Int
) {
    data class Header(
            @StringRes
            val text: Int,
            @StringRes
            val subtitle: Int? = null
    ) : RSSConfigureItem(R.layout.view_rss_configure_header)

    object NoItems : RSSConfigureItem(R.layout.view_rss_configure_no_items)

    data class Item(
            val url: String,
            val supportedArticleSource: SupportedArticleSource?
    ) : RSSConfigureItem(R.layout.view_rss_configure_item)

    data class QuickAdd(
            val supportedArticleSource: SupportedArticleSource
    ) : RSSConfigureItem(R.layout.view_rss_configure_quickadd)

    object Add : RSSConfigureItem(R.layout.view_rss_configure_add)
}