package tmg.flashback.statistics.ui.dashboard.season

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.ViewDashboardSeasonConstructorBinding
import tmg.flashback.statistics.databinding.ViewDashboardSeasonDriverBinding
import tmg.flashback.statistics.databinding.ViewDashboardSeasonTrackBinding
import tmg.flashback.statistics.ui.dashboard.season.viewholders.ConstructorViewHolder
import tmg.flashback.statistics.ui.dashboard.season.viewholders.DriverViewHolder
import tmg.flashback.statistics.ui.dashboard.season.viewholders.TrackViewHolder
import tmg.flashback.statistics.ui.shared.sync.SyncAdapter

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
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.view_dashboard_season_track -> TrackViewHolder(
                trackClicked,
                ViewDashboardSeasonTrackBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_dashboard_season_driver -> DriverViewHolder(
                driverClicked,
                ViewDashboardSeasonDriverBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_dashboard_season_constructor -> ConstructorViewHolder(
                constructorClicked,
                ViewDashboardSeasonConstructorBinding.inflate(layoutInflater, parent, false)
            )
            else -> super.onCreateViewHolder(parent, viewType)
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = list[position]) {
            is SeasonItem.Track -> (holder as TrackViewHolder).bind(item)
            is SeasonItem.Driver -> (holder as DriverViewHolder).bind(item)
            is SeasonItem.Constructor -> (holder as ConstructorViewHolder).bind(item)
            is SeasonItem.ErrorItem -> bindErrors(holder, item.item)
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