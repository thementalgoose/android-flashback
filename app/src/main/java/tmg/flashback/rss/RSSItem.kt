package tmg.flashback.rss

import androidx.annotation.LayoutRes
import tmg.flashback.R
import tmg.flashback.repo.models.rss.Article
import tmg.flashback.shared.sync.SyncDataItem

sealed class RSSItem(
    @LayoutRes val layoutId: Int
) {
    data class RSS(
        val item: Article
    ): RSSItem(R.layout.view_rss_item)

    data class Message(
        val msg: String
    ): RSSItem(R.layout.view_shared_message)

    data class ErrorItem(
        val item: SyncDataItem
    ): RSSItem(item.layoutId)

    object SourcesDisabled: RSSItem(R.layout.view_rss_sources_disabled)
}

fun MutableList<RSSItem>.addError(syncDataItem: SyncDataItem) {
    this.add(RSSItem.ErrorItem(syncDataItem))
}