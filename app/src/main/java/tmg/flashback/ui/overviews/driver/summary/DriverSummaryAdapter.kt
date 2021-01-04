package tmg.flashback.ui.overviews.driver.summary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.R
import tmg.flashback.ui.overviews.driver.summary.viewholders.HeaderViewHolder
import tmg.flashback.ui.overviews.viewholders.OverviewDriverHistoryViewHolder
import tmg.flashback.ui.overviews.viewholders.DriverHistoryViewHolder
import tmg.flashback.ui.overviews.viewholders.StatsViewHolder
import tmg.flashback.ui.race.viewholders.SkeletonLoadingViewHolder
import tmg.flashback.ui.shared.pill.PillItem
import tmg.flashback.ui.shared.sync.SyncAdapter
import tmg.flashback.ui.utils.GenericDiffCallback

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
        return when (viewType) {
            R.layout.view_driver_summary_header -> HeaderViewHolder(
                    pillClicked = {
                        when (it) {
                            is PillItem.Wikipedia -> openUrl(it.link)
                            else -> {} /* Not shown */
                        }
                    },
                    itemView = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            )
            R.layout.view_overview_stat -> StatsViewHolder(
                    LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            )
            R.layout.view_driver_summary_history -> when (seasonClicked != null) {
                true -> OverviewDriverHistoryViewHolder(
                        seasonClicked,
                        LayoutInflater.from(parent.context).inflate(viewType, parent, false)
                )
                false -> DriverHistoryViewHolder(
                        LayoutInflater.from(parent.context).inflate(viewType, parent, false)
                )
            }
            R.layout.view_loading_podium -> SkeletonLoadingViewHolder(
                    LayoutInflater.from(parent.context).inflate(viewType, parent, false)
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