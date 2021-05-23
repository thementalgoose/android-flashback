package tmg.flashback.statistics.ui.overview.driver.season

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.ViewDriverSeasonBinding
import tmg.flashback.statistics.databinding.ViewDriverSeasonHeaderBinding
import tmg.flashback.statistics.databinding.ViewDriverSummaryHistoryBinding
import tmg.flashback.statistics.databinding.ViewOverviewStatBinding
import tmg.flashback.statistics.ui.overview.viewholders.StatsViewHolder
import tmg.flashback.statistics.ui.overview.driver.season.viewholders.RaceHeaderViewHolder
import tmg.flashback.statistics.ui.overview.driver.season.viewholders.RaceViewHolder
import tmg.flashback.statistics.ui.overview.viewholders.DriverHistoryViewHolder
import tmg.flashback.statistics.ui.shared.sync.SyncAdapter
import tmg.flashback.statistics.ui.shared.sync.SyncDataItem
import tmg.utilities.difflist.GenericDiffCallback

@Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_OVERRIDE")
class DriverSeasonAdapter(
        private val itemClicked: (result: DriverSeasonItem.Result) -> Unit
): SyncAdapter<DriverSeasonItem>() {

    override var list: List<DriverSeasonItem> = emptyList()
        set(initialValue) {
            val value = initialValue.addDataProvidedByItem()
            val result = DiffUtil.calculateDiff(GenericDiffCallback(field, value))
            field = value
            result.dispatchUpdatesTo(this)
        }

    override fun dataProvidedItem(syncDataItem: SyncDataItem) = DriverSeasonItem.ErrorItem(syncDataItem)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.view_overview_stat -> StatsViewHolder(
                    ViewOverviewStatBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_driver_summary_history -> DriverHistoryViewHolder(
                    ViewDriverSummaryHistoryBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_driver_season -> RaceViewHolder(
                    itemClicked,
                    ViewDriverSeasonBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_driver_season_header -> RaceHeaderViewHolder(
                    ViewDriverSeasonHeaderBinding.inflate(layoutInflater, parent, false)
            )
            else -> super.onCreateViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = list[position]) {
            is DriverSeasonItem.Stat -> (holder as StatsViewHolder).bind(item)
            is DriverSeasonItem.Result -> (holder as RaceViewHolder).bind(item)
            is DriverSeasonItem.ErrorItem -> bindErrors(holder, item.item)
            is DriverSeasonItem.RacedFor -> (holder as DriverHistoryViewHolder).bind(item)
        }
    }

    override fun viewType(position: Int) = list[position].layoutId

}