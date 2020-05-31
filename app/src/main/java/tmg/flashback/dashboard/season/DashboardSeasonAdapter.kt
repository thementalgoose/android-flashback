package tmg.flashback.dashboard.season

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.R
import tmg.flashback.dashboard.season.DashboardSeasonViewType.*
import tmg.flashback.dashboard.season.viewholders.DashboardSeasonHeaderViewHolder
import tmg.flashback.dashboard.season.viewholders.DashboardSeasonTrackViewHolder
import tmg.utilities.extensions.toEnum

class DashboardSeasonAdapter(
    val itemClickedCallback: (seasonRound: DashboardSeasonAdapterItem.Track) -> Unit,
    val listClosed: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list: List<DashboardSeasonAdapterItem> = emptyList()
        set(value) {
            val result = DiffUtil.calculateDiff(DiffCallback(field, value))
            field = value
            result.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType.toEnum<DashboardSeasonViewType>()) {
            HEADER -> DashboardSeasonHeaderViewHolder(
                listClosed,
                LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.view_season_header, parent, false)
            )
            TRACK -> DashboardSeasonTrackViewHolder(
                itemClickedCallback,
                LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.view_season_track, parent, false)
            )
            else -> throw Throwable("Cannot create view with view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val type = getItemViewType(position).toEnum<DashboardSeasonViewType>()) {
            HEADER -> {
                (holder as? DashboardSeasonHeaderViewHolder)
                    ?.bind(list[position] as DashboardSeasonAdapterItem.Header)
            }
            TRACK -> {
                (holder as? DashboardSeasonTrackViewHolder)
                    ?.bind(list[position] as DashboardSeasonAdapterItem.Track)
            }
            else -> throw Throwable("Cannot bind view of view type $type")
        }
    }

    override fun getItemViewType(position: Int) = list[position].viewType.ordinal

    override fun getItemCount(): Int = list.size

    //region Comparator

    inner class DiffCallback(
        private val oldList: List<DashboardSeasonAdapterItem>,
        private val newList: List<DashboardSeasonAdapterItem>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition] == newList[newItemPosition]

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            areItemsTheSame(oldItemPosition, newItemPosition) &&
                    isHeaderDataTheSame(oldList[oldItemPosition], newList[newItemPosition]) &&
                    isTrackDataTheSame(oldList[oldItemPosition], newList[newItemPosition])

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        private fun isHeaderDataTheSame(i1: DashboardSeasonAdapterItem, i2: DashboardSeasonAdapterItem): Boolean {
            if (i1 is DashboardSeasonAdapterItem.Header && i2 is DashboardSeasonAdapterItem.Header) {
                return i1.year == i2.year
            }
            return true
        }

        private fun isTrackDataTheSame(i1: DashboardSeasonAdapterItem, i2: DashboardSeasonAdapterItem): Boolean {
            if (i1 is DashboardSeasonAdapterItem.Track && i2 is DashboardSeasonAdapterItem.Track) {
                return i1.season == i2.season && i1.round == i2.round
            }
            return true
        }
    }

    //endregion
}