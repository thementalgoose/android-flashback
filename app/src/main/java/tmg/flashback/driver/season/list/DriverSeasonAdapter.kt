package tmg.flashback.driver.season.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.R
import tmg.flashback.driver.season.list.viewholders.DriverHeaderViewHolder
import tmg.flashback.driver.season.list.viewholders.DriverSeasonHeaderViewHolder
import tmg.flashback.driver.season.list.viewholders.DriverSeasonViewHolder
import tmg.flashback.shared.viewholders.*
import tmg.flashback.utils.GenericDiffCallback

class DriverSeasonAdapter(
        private val itemClicked: (result: DriverSeasonItem.Result) -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list: List<DriverSeasonItem> = emptyList()
        set(value) {
            val result = DiffUtil.calculateDiff(GenericDiffCallback(field, value))
            field = value
            result.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.view_driver_header -> DriverHeaderViewHolder(
                    LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            )
            R.layout.view_driver_season -> DriverSeasonViewHolder(
                    itemClicked,
                    LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            )
            R.layout.view_driver_season_header -> DriverSeasonHeaderViewHolder(
                    LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            )
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
            else -> throw Exception("Type in DriverSeasonItem is not implemented on onCreateViewHolder")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = list[position]) {
            is DriverSeasonItem.Header -> (holder as DriverHeaderViewHolder).bind(item)
            is DriverSeasonItem.Result -> (holder as DriverSeasonViewHolder).bind(item)
            is DriverSeasonItem.Unavailable -> (holder as DataUnavailableViewHolder).bind(item.type)
            is DriverSeasonItem.Message -> (holder as MessageViewHolder).bind(item.msg)
        }
    }

    override fun getItemCount() = list.size

    override fun getItemViewType(position: Int) = list[position].layoutId
}