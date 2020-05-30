package tmg.flashback.standings

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.dashboard.year.DashboardYearItem

class StandingsAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list: List<StandingsItem> = emptyList()
        set(value) {
            val result = DiffUtil.calculateDiff(DiffUtilComparator(field, value))
            field = value
            result.dispatchUpdatesTo(this)
        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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