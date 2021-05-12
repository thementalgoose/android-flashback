package tmg.flashback.statistics.ui.overview.driver.summary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.SkeletonRaceBinding
import tmg.flashback.statistics.databinding.ViewDriverSummaryHeaderBinding
import tmg.flashback.statistics.databinding.ViewDriverSummaryHistoryBinding
import tmg.flashback.statistics.databinding.ViewOverviewStatBinding
import tmg.flashback.statistics.ui.overview.driver.summary.viewholders.HeaderViewHolder
import tmg.flashback.statistics.ui.overview.viewholders.OverviewDriverHistoryViewHolder
import tmg.flashback.statistics.ui.overview.viewholders.DriverHistoryViewHolder
import tmg.flashback.statistics.ui.overview.viewholders.StatsViewHolder
import tmg.flashback.statistics.ui.race.viewholders.SkeletonLoadingViewHolder
import tmg.flashback.statistics.ui.shared.pill.PillItem
import tmg.flashback.statistics.ui.shared.sync.SyncAdapter
import tmg.utilities.difflist.GenericDiffCallback

@Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_OVERRIDE")
class DriverSummaryAdapter(
    val openUrl: (String) -> Unit,
    val seasonClicked: ((Int) -> Unit)? = null
): SyncAdapter<DriverSummaryItem>() {

    override var list: List<DriverSummaryItem> = listOf(DriverSummaryItem.Loading)
        set(value) {
            val result = DiffUtil.calculateDiff(GenericDiffCallback(field, value))
            field = value
            result.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.view_driver_summary_header -> HeaderViewHolder(
                    pillClicked = {
                        when (it) {
                            is PillItem.Wikipedia -> openUrl(it.link)
                            else -> {} /* Not shown */
                        }
                    },
                    binding = ViewDriverSummaryHeaderBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_overview_stat -> StatsViewHolder(
                    ViewOverviewStatBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_driver_summary_history -> when (seasonClicked != null) {
                true -> OverviewDriverHistoryViewHolder(
                        seasonClicked,
                        ViewDriverSummaryHistoryBinding.inflate(layoutInflater, parent, false)
                )
                false -> DriverHistoryViewHolder(
                        ViewDriverSummaryHistoryBinding.inflate(layoutInflater, parent, false)
                )
            }
            R.layout.view_loading_podium -> SkeletonLoadingViewHolder(
                    SkeletonRaceBinding.inflate(layoutInflater, parent, false)
            )
            else -> super.onCreateViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = list[position]) {
            is DriverSummaryItem.Header -> (holder as HeaderViewHolder).bind(item)
            is DriverSummaryItem.Stat -> (holder as StatsViewHolder).bind(item)
            is DriverSummaryItem.RacedFor -> (holder as OverviewDriverHistoryViewHolder).bind(item)
            is DriverSummaryItem.ErrorItem -> bindErrors(holder, item.item)
        }
    }

    override fun viewType(position: Int) = list[position].layoutId

}