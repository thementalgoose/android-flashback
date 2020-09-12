package tmg.flashback.driver.overview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.R
import tmg.flashback.driver.overview.viewholders.HeaderViewHolder
import tmg.flashback.driver.overview.viewholders.RacedForViewHolder
import tmg.flashback.driver.overview.viewholders.StatsViewHolder
import tmg.flashback.shared.SyncAdapter
import tmg.flashback.utils.GenericDiffCallback

class DriverOverviewAdapter: SyncAdapter<DriverOverviewItem>() {

    override var list: List<DriverOverviewItem> = emptyList()
        set(value) {
            val result = DiffUtil.calculateDiff(GenericDiffCallback(field, value))
            field = value
            result.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.view_driver_overview_header -> HeaderViewHolder(
                    LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            )
            R.layout.view_driver_overview_stat -> StatsViewHolder(
                    LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            )
            R.layout.view_driver_overview_raced_for -> RacedForViewHolder(
                    LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            )
            else -> super.onCreateViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = list[position]) {
            is DriverOverviewItem.Header -> (holder as HeaderViewHolder).bind(item)
            is DriverOverviewItem.Stat -> (holder as StatsViewHolder).bind(item)
            is DriverOverviewItem.RacedFor -> (holder as RacedForViewHolder).bind(item)
            is DriverOverviewItem.ErrorItem -> bindErrors(holder, item.item)
        }
    }

    override fun viewType(position: Int) = list[position].layoutId

}