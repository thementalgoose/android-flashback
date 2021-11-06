package tmg.flashback.statistics.ui.overview.constructor.summary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.*
import tmg.flashback.statistics.ui.overview.constructor.summary.viewholders.ConstructorListHeaderViewHolder
import tmg.flashback.statistics.ui.overview.constructor.summary.viewholders.HeaderViewHolder
import tmg.flashback.statistics.ui.overview.constructor.summary.viewholders.ConstructorHistoryViewHolder
import tmg.flashback.statistics.ui.overview.viewholders.StatsViewHolder
import tmg.flashback.statistics.ui.race.viewholders.SkeletonLoadingViewHolder
import tmg.flashback.statistics.ui.shared.pill.PillItem
import tmg.flashback.statistics.ui.shared.sync.SyncAdapter
import tmg.flashback.statistics.ui.shared.sync.SyncDataItem
import tmg.utilities.difflist.GenericDiffCallback

@Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_OVERRIDE")
class ConstructorSummaryAdapter(
        private val openUrl: (String) -> Unit,
        private val openSeason: (Int) -> Unit
): SyncAdapter<ConstructorSummaryItem>() {

    override var list: List<ConstructorSummaryItem> = emptyList()
        set(value) {
            val result = DiffUtil.calculateDiff(GenericDiffCallback(field, value))
            field = value
            result.dispatchUpdatesTo(this)
        }

    override fun dataProvidedItem(syncDataItem: SyncDataItem) = ConstructorSummaryItem.ErrorItem(syncDataItem)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.view_constructor_summary_header -> HeaderViewHolder(
                    pillClicked = {
                        when (it) {
                            is PillItem.Wikipedia -> openUrl(it.link)
                            else -> {} /* Do nothing */
                        }
                    },
                    binding = ViewConstructorSummaryHeaderBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_overview_stat -> StatsViewHolder(
                    ViewOverviewStatBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_loading_podium -> SkeletonLoadingViewHolder(
                    SkeletonRaceBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_constructor_summary_list_header -> ConstructorListHeaderViewHolder(
                    ViewConstructorSummaryListHeaderBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_constructor_summary_history -> ConstructorHistoryViewHolder(
                    ViewConstructorSummaryHistoryBinding.inflate(layoutInflater, parent, false)
            )
            else -> super.onCreateViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = list[position]) {
            is ConstructorSummaryItem.Header -> (holder as HeaderViewHolder).bind(item)
            is ConstructorSummaryItem.Stat -> (holder as StatsViewHolder).bind(item)
            is ConstructorSummaryItem.History -> (holder as ConstructorHistoryViewHolder).bind(item)
            is ConstructorSummaryItem.ErrorItem -> bindErrors(holder, item.item)
        }
    }

    override fun viewType(position: Int) = list[position].layoutId
}