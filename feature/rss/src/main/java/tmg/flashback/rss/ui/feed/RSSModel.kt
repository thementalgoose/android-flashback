package tmg.flashback.rss.ui.feed

import androidx.annotation.LayoutRes
import tmg.flashback.rss.R
import tmg.flashback.rss.repo.model.Article

sealed class RSSModel(
    val key: String,
    @LayoutRes val layoutId: Int
) {
    data class RSS(
        val item: Article
    ): RSSModel(
        key = item.id,
        layoutId = R.layout.view_rss_item
    )

    data class Message(
        val msg: String,
        val id: String = msg,
    ): RSSModel(
        key = id,
        layoutId = R.layout.view_rss_message
    )

    object Advert: RSSModel(
        key = "advert",
        layoutId = R.layout.view_rss_advert
    )

    object NoNetwork: RSSModel(
        key = "advert",
        layoutId = R.layout.view_rss_no_network
    )

    object InternalError: RSSModel(
        key = "advert",
        layoutId = R.layout.view_rss_internal_error
    )

    object SourcesDisabled: RSSModel(
        key = "advert",
        layoutId = R.layout.view_rss_sources_disabled
    )
}