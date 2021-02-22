package tmg.flashback.statistics.ui.overview.driver.season

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.koin.core.component.KoinApiExtension
import tmg.flashback.core.ui.shared.GenericDiffCallback
import tmg.flashback.statistics.R
import tmg.flashback.statistics.ui.overview.viewholders.StatsViewHolder
import tmg.flashback.statistics.ui.overview.driver.season.viewholders.RaceHeaderViewHolder
import tmg.flashback.statistics.ui.overview.driver.season.viewholders.RaceViewHolder
import tmg.flashback.statistics.ui.overview.viewholders.DriverHistoryViewHolder
import tmg.flashback.statistics.ui.shared.sync.SyncAdapter

@Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_OVERRIDE")
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
            R.layout.view_overview_stat -> StatsViewHolder(
                    LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            )
            R.layout.view_driver_summary_history -> DriverHistoryViewHolder(
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
            is DriverSeasonItem.RacedFor -> (holder as DriverHistoryViewHolder).bind(item)
        }
    }

    override fun viewType(position: Int) = list[position].layoutId

}