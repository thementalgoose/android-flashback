package tmg.flashback.overviews.constructor.summary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.R
import tmg.flashback.overviews.constructor.summary.viewholders.HeaderViewHolder
import tmg.flashback.overviews.viewholders.StatsViewHolder
import tmg.flashback.race.viewholders.SkeletonLoadingViewHolder
import tmg.flashback.shared.pill.PillItem
import tmg.flashback.shared.sync.SyncAdapter
import tmg.flashback.utils.GenericDiffCallback

class ConstructorSummaryAdapter(
        private val openUrl: (String) -> Unit,
        private val openSeason: (Int) -> Unit
): SyncAdapter<ConstructorSummaryItem>() {

    override var list: List<ConstructorSummaryItem> = listOf(ConstructorSummaryItem.Loading)
        set(value) {
            val result = DiffUtil.calculateDiff(GenericDiffCallback(field, value))
            field = value
            result.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.view_constructor_summary_header -> HeaderViewHolder(
                    pillClicked = {
                        when (it) {
                            is PillItem.Wikipedia -> openUrl(it.link)
                            else -> {} /* Do nothing */
                        }
                    },
                    itemView = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            )
            R.layout.view_overview_stat -> StatsViewHolder(
                    LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            )
            R.layout.view_loading_podium -> SkeletonLoadingViewHolder(
                    LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            )
            else -> super.onCreateViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = list[position]) {
            is ConstructorSummaryItem.Header -> (holder as HeaderViewHolder).bind(item)
            is ConstructorSummaryItem.Stat -> (holder as StatsViewHolder).bind(item)
        }
    }

    override fun viewType(position: Int) = list[position].layoutId
}