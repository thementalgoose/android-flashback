package tmg.flashback.shared.sync

import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import tmg.flashback.R
import tmg.flashback.shared.viewholders.DataUnavailable

sealed class SyncDataItem(
    @LayoutRes val layoutId: Int
) {
    object NoNetwork: SyncDataItem(R.layout.view_shared_no_network)

    object ConstructorsChampionshipNotAwarded: SyncDataItem(R.layout.view_shared_constructor_championship_not_awarded)

    object InternalError: SyncDataItem(R.layout.view_shared_internal_error)

    object AllSourcesDisabled: SyncDataItem(R.layout.view_shared_no_news_sources)

    data class Message(
            val msg: String
    ): SyncDataItem(R.layout.view_shared_message)

    data class MessageRes(
            @StringRes
            val msg: Int,
            val values: List<Any> = emptyList()
    ): SyncDataItem(R.layout.view_shared_message)

    data class Unavailable(
        val type: DataUnavailable
    ): SyncDataItem(R.layout.view_shared_data_unavailable)
}