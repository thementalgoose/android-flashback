package tmg.flashback.shared.sync

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.R
import tmg.flashback.shared.viewholders.*

abstract class SyncAdapter<T>: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    abstract var list: List<T>

    abstract fun viewType(position: Int): Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.view_shared_data_unavailable -> DataUnavailableViewHolder(view)
            R.layout.view_shared_no_network -> NoNetworkViewHolder(view)
            R.layout.view_shared_internal_error -> InternalErrorOccurredViewHolder(view)
            R.layout.view_shared_message -> MessageViewHolder(view)
            R.layout.view_shared_constructor_championship_not_awarded -> ConstructorsChampionshipNotAwardedViewHolder(view)
            R.layout.view_shared_provided -> ProvidedByViewHolder(view)
            else -> throw Error("${this.javaClass.simpleName} Does not have a supported layout id to create a viewholder by")
        }
    }

    fun bindErrors(holder: RecyclerView.ViewHolder, item: SyncDataItem) {
        when (item) {
            is SyncDataItem.Unavailable -> (holder as DataUnavailableViewHolder).bind(item.type)
            is SyncDataItem.Message -> (holder as MessageViewHolder).bind(item.msg)
            is SyncDataItem.MessageRes -> (holder as MessageViewHolder).bind(item.msg, item.values)
            else -> {}
        }
    }

    override fun getItemCount() = list.size

    override fun getItemViewType(position: Int) = viewType(position)

}