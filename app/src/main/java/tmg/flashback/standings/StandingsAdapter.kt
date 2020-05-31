package tmg.flashback.standings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.dashboard.year.DashboardYearItem
import tmg.flashback.standings.StandingsItemType.*
import tmg.flashback.standings.viewholder.StandingsConstructorViewHolder
import tmg.flashback.standings.viewholder.StandingsDriverViewHolder
import tmg.flashback.standings.viewholder.StandingsHeaderViewHolder
import tmg.utilities.extensions.toEnum

class StandingsAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list: List<StandingsItem> = emptyList()
        set(value) {
            val result = DiffUtil.calculateDiff(DiffUtilComparator(field, value))
            field = value
            result.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (val type = viewType.toEnum<StandingsItemType>() ?: HEADER) {
            HEADER -> StandingsHeaderViewHolder(
                LayoutInflater.from(parent.context).inflate(type.layoutId, parent, false)
            )
            DRIVER -> StandingsDriverViewHolder(
                LayoutInflater.from(parent.context).inflate(type.layoutId, parent, false)
            )
            CONSTRUCTOR -> StandingsConstructorViewHolder(
                LayoutInflater.from(parent.context).inflate(type.layoutId, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position).toEnum<StandingsItemType>()) {
            HEADER -> (holder as? StandingsHeaderViewHolder)?.bind(list[position] as StandingsItem.Header)
            DRIVER -> (holder as? StandingsDriverViewHolder)?.bind(list[position] as StandingsItem.Driver)
            CONSTRUCTOR -> (holder as? StandingsConstructorViewHolder)?.bind(list[position] as StandingsItem.Constructor)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            is StandingsItem.Header -> HEADER.ordinal
            is StandingsItem.Driver -> DRIVER.ordinal
            is StandingsItem.Constructor -> CONSTRUCTOR.ordinal
        }
    }

    override fun getItemCount(): Int = list.size

    //region Comparator

    inner class DiffUtilComparator(
        private val oldList: List<StandingsItem>,
        private val newList: List<StandingsItem>
    ): DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldList[oldItemPosition] == newList[newItemPosition]

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = areItemsTheSame(oldItemPosition, newItemPosition)

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size
    }

    //endregion

}