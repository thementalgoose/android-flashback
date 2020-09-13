package tmg.flashback.driver.season

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.R
import tmg.flashback.driver.overview.DriverOverviewItem
import tmg.flashback.driver.viewholders.StatsViewHolder
import tmg.flashback.driver.season.viewholders.RaceHeaderViewHolder
import tmg.flashback.driver.season.viewholders.RaceViewHolder
import tmg.flashback.driver.viewholders.RacedForViewHolder
import tmg.flashback.shared.SyncAdapter
import tmg.flashback.utils.GenericDiffCallback

class DriverSeasonAdapter(
        private val itemClicked: (result: DriverSeasonItem.Result) -> Unit
): SyncAdapter<DriverSeasonItem>() {

    override var list: List<DriverSeasonItem> = emptyList()
        set(value) {
            val result = DiffUtil.calculateDiff(GenericDiffCallback(field, value))
            field = value
            result.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.view_driver_overview_stat -> StatsViewHolder(
                    LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            )
            R.layout.view_driver_overview_raced_for -> RacedForViewHolder(
                    LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            )
            R.layout.view_driver_season -> RaceViewHolder(
                    itemClicked,
                    LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            )
            R.layout.view_driver_season_header -> RaceHeaderViewHolder(
                    LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            )
            else -> super.onCreateViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = list[position]) {
            is DriverSeasonItem.Stat -> (holder as StatsViewHolder).bind(item)
            is DriverSeasonItem.Result -> (holder as RaceViewHolder).bind(item)
            is DriverSeasonItem.ErrorItem -> bindErrors(holder, item.item)
            is DriverSeasonItem.RacedFor -> (holder as RacedForViewHolder).bind(item)
        }
    }

    override fun viewType(position: Int) = list[position].layoutId

}