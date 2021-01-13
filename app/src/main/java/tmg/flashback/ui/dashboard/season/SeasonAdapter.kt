package tmg.flashback.ui.dashboard.season

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.R
import tmg.flashback.ui.dashboard.season.viewholders.ConstructorViewHolder
import tmg.flashback.ui.dashboard.season.viewholders.DriverViewHolder
import tmg.flashback.ui.dashboard.season.viewholders.GraphViewHolder
import tmg.flashback.ui.dashboard.season.viewholders.TrackViewHolder
import tmg.flashback.ui.shared.sync.SyncAdapter

class SeasonAdapter(
    private val trackClicked: (track: SeasonItem.Track) -> Unit,
    private val driverClicked: (driver: SeasonItem.Driver) -> Unit,
    private val constructorClicked: (constructor: SeasonItem.Constructor) -> Unit
): SyncAdapter<SeasonItem>() {

    override var list: List<SeasonItem> = emptyList()
        set(value) {
            val result = DiffUtil.calculateDiff(DiffCallback(field, value))
            field = value
            result.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.view_dashboard_season_track -> TrackViewHolder(trackClicked, view)
            R.layout.view_dashboard_season_driver -> DriverViewHolder(driverClicked, view)
            R.layout.view_dashboard_season_constructor -> ConstructorViewHolder(constructorClicked, view)
            R.layout.view_dashboard_season_graph -> GraphViewHolder(view)
            else -> super.onCreateViewHolder(parent, viewType)
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = list[position]) {
            is SeasonItem.Track -> (holder as TrackViewHolder).bind(item)
            is SeasonItem.Driver -> (holder as DriverViewHolder).bind(item)
            is SeasonItem.Constructor -> (holder as ConstructorViewHolder).bind(item)
            is SeasonItem.ErrorItem -> bindErrors(holder, item.item)
            is SeasonItem.Graph -> (holder as GraphViewHolder).bind()
        }
    }

    override fun viewType(position: Int) = list[position].layoutId

    inner class DiffCallback(
        private val oldList: List<SeasonItem>,
        private val newList: List<SeasonItem>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(o: Int, n: Int) = oldList[o] == newList[n] ||
                areTracksTheSame(o, n)

        override fun areContentsTheSame(o: Int, n: Int) = oldList[o] == newList[n]

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        private fun areTracksTheSame(old: Int, new: Int): Boolean {
            val oldItem = oldList[old] as? SeasonItem.Track
            val newItem = newList[new] as? SeasonItem.Track
            if (oldItem != null && newItem != null) {
                return oldItem.raceName == newItem.raceName &&
                        oldItem.raceCountryISO == newItem.raceCountryISO
            }
            return false
        }
    }
}