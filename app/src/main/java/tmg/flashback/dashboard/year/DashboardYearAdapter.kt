package tmg.flashback.dashboard.year

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.R

class DashboardYearAdapter(
    val itemClicked: (model: DashboardYearItem, itemId: Long) -> Unit
): RecyclerView.Adapter<DashboardYearViewHolder>() {

    var list: List<DashboardYearItem> = emptyList()
        set(value) {
            val result = DiffUtil.calculateDiff(DiffUtilComparator(field, value))
            field = value
            result.dispatchUpdatesTo(this)
        }

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int) = position.toLong()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardYearViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_dashboard_year, parent, false)
        return DashboardYearViewHolder(view, itemClicked)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: DashboardYearViewHolder, position: Int) {
        holder.bind(list[position], getItemId(position))
    }

    inner class DiffUtilComparator(
        private val oldList: List<DashboardYearItem>,
        private val newList: List<DashboardYearItem>
    ): DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldList[oldItemPosition] == newList[newItemPosition]

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = areItemsTheSame(oldItemPosition, newItemPosition)

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size
    }
}