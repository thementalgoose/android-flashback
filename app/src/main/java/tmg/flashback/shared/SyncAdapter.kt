package tmg.flashback.shared

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.R
import tmg.flashback.shared.viewholders.*

abstract class SyncAdapter<T>: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    abstract var list: List<T>

    abstract fun viewType(position: Int): Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.view_shared_data_unavailable -> DataUnavailableViewHolder(
                LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            )
            R.layout.view_shared_no_network -> NoNetworkViewHolder(
                LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            )
            R.layout.view_shared_no_news_sources -> NoNewsSourcesViewHolder(
                LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            )
            R.layout.view_shared_internal_error -> InternalErrorOccurredViewHolder(
                LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            )
            R.layout.view_shared_message -> MessageViewHolder(
                LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            )
            else -> throw Error("${this.javaClass.simpleName} Does not have a supported layout id to create a viewholder by")
        }
    }

    fun bindErrors(holder: RecyclerView.ViewHolder, syncDataItem: SyncDataItem) {
        when (val item = syncDataItem) {
            is SyncDataItem.Unavailable -> (holder as DataUnavailableViewHolder).bind(item.type)
            is SyncDataItem.Message -> (holder as MessageViewHolder).bind(item.msg)
            is SyncDataItem.MessageRes -> (holder as MessageViewHolder).bind(item.msg, item.values)
        }
    }

    override fun getItemCount() = list.size

    override fun getItemViewType(position: Int) = viewType(position)

}