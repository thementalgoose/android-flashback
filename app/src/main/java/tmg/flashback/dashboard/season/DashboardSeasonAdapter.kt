package tmg.flashback.dashboard.season

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.R
import tmg.flashback.dashboard.season.DashboardSeasonViewType.*
import tmg.flashback.dashboard.season.viewholders.DashboardSeasonConstructorViewHolder
import tmg.flashback.dashboard.season.viewholders.DashboardSeasonDriversViewHolder
import tmg.flashback.dashboard.season.viewholders.DashboardSeasonTrackViewHolder
import tmg.flashback.utils.SeasonRound
import tmg.utilities.extensions.toEnum

class DashboardSeasonAdapter(
    val itemClickedCallback: (seasonRound: DashboardSeasonAdapterItem.Track) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list: List<DashboardSeasonAdapterItem> = emptyList()
        set(value) {
            val result = DiffUtil.calculateDiff(DiffCallback(field, value))
            field = value
            result.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType.toEnum<DashboardSeasonViewType>()) {
            TRACK -> DashboardSeasonTrackViewHolder(
                itemClickedCallback,
                LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.view_dashboard_season_track, parent, false)
            )
            DRIVER -> DashboardSeasonDriversViewHolder(
                LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.view_dashboard_season_driver, parent, false)
            )
            CONSTRUCTOR -> DashboardSeasonDriversViewHolder(
                LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.view_dashboard_season_constructor, parent, false)
            )
            else -> throw Throwable("Cannot create view with view type $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].viewType.ordinal
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val type = getItemViewType(position).toEnum<DashboardSeasonViewType>()) {
            TRACK -> {
                (holder as? DashboardSeasonTrackViewHolder)
                    ?.bind(list[position] as DashboardSeasonAdapterItem.Track)
            }
            DRIVER -> {
                (holder as? DashboardSeasonDriversViewHolder)
                    ?.bind(list[position] as DashboardSeasonAdapterItem.Drivers)
            }
            CONSTRUCTOR -> {
                (holder as? DashboardSeasonConstructorViewHolder)
                    ?.bind(list[position] as DashboardSeasonAdapterItem.Constructor)
            }
            else -> throw Throwable("Cannot bind view of view type $type")
        }
    }

    override fun getItemCount(): Int = list.size

    //region Comparator

    inner class DiffCallback(
        private val oldList: List<DashboardSeasonAdapterItem>,
        private val newList: List<DashboardSeasonAdapterItem>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition] == newList[newItemPosition]

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            areItemsTheSame(oldItemPosition, newItemPosition)

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size
    }

    //endregion
}