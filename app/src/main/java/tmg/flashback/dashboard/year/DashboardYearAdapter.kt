package tmg.flashback.dashboard.year

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.R
import tmg.flashback.dashboard.year.viewholders.DashboardHeaderViewHolder
import tmg.flashback.dashboard.year.viewholders.DashboardYearViewHolder
import tmg.utilities.extensions.toEnum

class DashboardYearAdapter(
    private val itemClicked: (model: DashboardYearItem.Season, itemId: Long) -> Unit,
    private val settingsClicked: () -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list: List<DashboardYearItem> = emptyList()
        set(value) {
            val result = DiffUtil.calculateDiff(DiffUtilComparator(field, value))
            field = value
            result.dispatchUpdatesTo(this)
        }

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType.toEnum<DashboardViewType>()) {
            DashboardViewType.SEASON -> {
                val layout = LayoutInflater.from(parent.context).inflate(R.layout.view_dashboard_year, parent, false)
                DashboardYearViewHolder(layout, itemClicked)
            }
            DashboardViewType.HEADER -> {
                DashboardHeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_dashboard_header, parent, false), settingsClicked)
            }
            null -> throw Error("View type not supported")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position).toEnum<DashboardViewType>()) {
            DashboardViewType.SEASON -> (holder as? DashboardYearViewHolder)?.bind(list[position] as DashboardYearItem.Season, getItemId(position))
            DashboardViewType.HEADER -> {}
        }
    }

    override fun getItemViewType(position: Int) = when (list[position]) {
        is DashboardYearItem.Season -> DashboardViewType.SEASON.ordinal
        DashboardYearItem.Header -> DashboardViewType.HEADER.ordinal
    }

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemCount(): Int = list.size

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