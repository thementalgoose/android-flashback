package tmg.flashback.statistics.ui.dashboard.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.*
import tmg.flashback.statistics.ui.dashboard.list.viewholders.*

class ListAdapter(
    val settingsClicked: () -> Unit,
    var featureToggled: (type: HeaderType) -> Unit,
    var favouriteToggled: (season: Int) -> Unit,
    var seasonClicked: (season: Int) -> Unit,
    var setDefaultClicked: (season: Int) -> Unit,
    var clearDefaultClicked: () -> Unit,
    val buttonClicked: (String) -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list: List<ListItem> = emptyList()
        set(value) {
            val result = DiffUtil.calculateDiff(DiffCallback(field, value))
            field = value
            result.dispatchUpdatesTo(this)
        }

    fun refreshUpNext() {
        val index = list.indexOfFirst { it is ListItem.UpNext }
        if (index != -1) {
            notifyItemChanged(index)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.view_season_list_divider -> DividerViewHolder(
                ViewSeasonListDividerBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_season_list_season -> SeasonViewHolder(
                favouriteToggled,
                seasonClicked,
                setDefaultClicked,
                clearDefaultClicked,
                ViewSeasonListSeasonBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_season_list_header -> HeaderViewHolder(
                featureToggled,
                ViewSeasonListHeaderBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_season_list_hero -> HeroViewHolder(
                ViewSeasonListHeroBinding.inflate(layoutInflater, parent, false),
                settingsClicked
            )
            R.layout.view_season_list_up_next -> UpNextViewHolder(
                ViewSeasonListUpNextBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_season_list_button -> ButtonViewHolder(
                buttonClicked,
                ViewSeasonListButtonBinding.inflate(layoutInflater, parent, false)
            )
            else -> throw Exception("View type not implemented")
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        when (val item = list[position]) {
            is ListItem.Season -> (holder as SeasonViewHolder).bind(item)
            is ListItem.Header -> (holder as HeaderViewHolder).bind(item)
            is ListItem.UpNext -> (holder as UpNextViewHolder).bind(item)
            is ListItem.Button -> (holder as ButtonViewHolder).bind(item)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        this.onBindViewHolder(holder, position, mutableListOf())
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].layoutId
    }

    override fun getItemCount() = list.size

    inner class DiffCallback(
        private val oldList: List<ListItem>,
        private val newList: List<ListItem>
    ): DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem == newItem ||
                    itemsAreHeadersWithOnlyDifferentToggleState(oldItem, newItem) ||
                    itemsAreSeasonAndDefaultIsOnlyDifferent(oldItem, newItem)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        private fun itemsAreHeadersWithOnlyDifferentToggleState(oldItem: ListItem, newItem: ListItem): Boolean {
            return oldItem is ListItem.Header && newItem is ListItem.Header && oldItem.type == newItem.type
        }

        private fun itemsAreSeasonAndDefaultIsOnlyDifferent(oldItem: ListItem, newItem: ListItem): Boolean {
            return oldItem is ListItem.Season && newItem is ListItem.Season && oldItem.season == newItem.season
        }
    }
}