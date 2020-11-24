package tmg.flashback.rss.ui

import androidx.annotation.LayoutRes
import tmg.flashback.repo.models.rss.Article
import tmg.flashback.rss.R

sealed class RSSItem(
    @LayoutRes val layoutId: Int
) {
    data class RSS(
        val item: Article
    ): RSSItem(R.layout.view_rss_item)

    data class Message(
        val msg: String
    ): RSSItem(R.layout.view_rss_message)

    object NoNetwork: RSSItem(R.layout.view_rss_no_network)

    object InternalError: RSSItem(R.layout.view_rss_internal_error)

    object SourcesDisabled: RSSItem(R.layout.view_rss_sources_disabled)
}