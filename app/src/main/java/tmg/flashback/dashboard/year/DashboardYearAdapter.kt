package tmg.flashback.dashboard.year

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.R
import tmg.flashback.dashboard.year.viewholders.DashboardBannerViewHolder
import tmg.flashback.dashboard.year.viewholders.DashboardHeaderViewHolder
import tmg.flashback.dashboard.year.viewholders.DashboardSkeletonViewHolder
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
        val dashboardViewType = viewType.toEnum<DashboardViewType>()!!
        val view = LayoutInflater.from(parent.context).inflate(dashboardViewType.layoutId, parent, false)
        return when (dashboardViewType) {
            DashboardViewType.SEASON -> {
                DashboardYearViewHolder(view, itemClicked)
            }
            DashboardViewType.HEADER -> {
                DashboardHeaderViewHolder(view, settingsClicked)
            }
            DashboardViewType.PLACEHOLDER -> {
                DashboardSkeletonViewHolder(view)
            }
            DashboardViewType.BANNER -> {
                DashboardBannerViewHolder(view)
            }
            null -> throw Error("View type not supported")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position).toEnum<DashboardViewType>()) {
            DashboardViewType.SEASON -> (holder as? DashboardYearViewHolder)?.bind(list[position] as DashboardYearItem.Season, getItemId(position))
            DashboardViewType.BANNER -> (holder as? DashboardBannerViewHolder)?.bind((list[position] as DashboardYearItem.Banner).message)
            DashboardViewType.HEADER -> {}
        }
    }

    override fun getItemViewType(position: Int) = when (list[position]) {
        is DashboardYearItem.Season -> DashboardViewType.SEASON.ordinal
        is DashboardYearItem.Banner -> DashboardViewType.BANNER.ordinal
        DashboardYearItem.Placeholder -> DashboardViewType.PLACEHOLDER.ordinal
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