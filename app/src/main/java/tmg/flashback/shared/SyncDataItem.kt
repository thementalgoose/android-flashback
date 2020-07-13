package tmg.flashback.shared

import androidx.annotation.LayoutRes
import tmg.flashback.R
import tmg.flashback.news.NewsItem
import tmg.flashback.shared.viewholders.DataUnavailable

sealed class SyncDataItem(
    @LayoutRes val layoutId: Int
) {
    object NoNetwork: SyncDataItem(R.layout.view_shared_no_network)

    object InternalError: SyncDataItem(R.layout.view_shared_internal_error)

    object AllSourcesDisabled: SyncDataItem(R.layout.view_shared_no_news_sources)

    data class Message(
        val msg: String
    ): SyncDataItem(R.layout.view_shared_message)

    data class Unavailable(
        val type: DataUnavailable
    ): SyncDataItem(R.layout.view_shared_data_unavailable)
}