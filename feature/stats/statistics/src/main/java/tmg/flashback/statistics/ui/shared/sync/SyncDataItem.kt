package tmg.flashback.statistics.ui.shared.sync

import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import tmg.flashback.statistics.R
import tmg.flashback.statistics.ui.shared.sync.viewholders.DataUnavailable

sealed class SyncDataItem(
    @LayoutRes val layoutId: Int
) {
    object PullRefresh: SyncDataItem(R.layout.view_shared_no_network)

    object NoNetwork: SyncDataItem(R.layout.view_shared_no_network)

    object ConstructorsChampionshipNotAwarded: SyncDataItem(R.layout.view_shared_constructor_championship_not_awarded)

    object InternalError: SyncDataItem(R.layout.view_shared_internal_error)

    data class Message(
            val msg: String,
            val clickUrl: String? = null
    ): SyncDataItem(R.layout.view_shared_message)

    data class MessageRes(
            @StringRes
            val msg: Int,
            val values: List<Any> = emptyList(),
            val clickUrl: String? = null
    ): SyncDataItem(R.layout.view_shared_message)

    data class Unavailable(
        val type: DataUnavailable
    ): SyncDataItem(R.layout.view_shared_data_unavailable)

    data class ProvidedBy(
        val uuid: String = "provided_by"
    ): SyncDataItem(R.layout.view_shared_provided)

    object Skeleton : SyncDataItem(R.layout.view_shared_skeleton)
}