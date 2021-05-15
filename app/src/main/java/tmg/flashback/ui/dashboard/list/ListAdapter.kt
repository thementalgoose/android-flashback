package tmg.flashback.ui.dashboard.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.R
import tmg.flashback.databinding.ViewSeasonListButtonBinding
import tmg.flashback.databinding.ViewSeasonListDividerBinding
import tmg.flashback.databinding.ViewSeasonListHeaderBinding
import tmg.flashback.databinding.ViewSeasonListHeroBinding
import tmg.flashback.databinding.ViewSeasonListSeasonBinding
import tmg.flashback.databinding.ViewSeasonListUpNextBinding
import tmg.flashback.upnext.repository.model.TimeListDisplayType

class ListAdapter(
    var featureToggled: (type: HeaderType) -> Unit,
    var favouriteToggled: (season: Int) -> Unit,
    var seasonClicked: (season: Int) -> Unit,
    var setDefaultClicked: (season: Int) -> Unit,
    var clearDefaultClicked: () -> Unit,
    val buttonClicked: (String) -> Unit,
    val timeDisplayFormatClicked: (TimeListDisplayType) -> Unit
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
            R.layout.view_season_list_divider -> tmg.flashback.ui.dashboard.list.viewholders.DividerViewHolder(
                ViewSeasonListDividerBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_season_list_season -> tmg.flashback.ui.dashboard.list.viewholders.SeasonViewHolder(
                favouriteToggled,
                seasonClicked,
                setDefaultClicked,
                clearDefaultClicked,
                ViewSeasonListSeasonBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_season_list_header -> tmg.flashback.ui.dashboard.list.viewholders.HeaderViewHolder(
                featureToggled,
                ViewSeasonListHeaderBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_season_list_hero -> tmg.flashback.ui.dashboard.list.viewholders.HeroViewHolder(
                ViewSeasonListHeroBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_season_list_up_next -> tmg.flashback.ui.dashboard.list.viewholders.UpNextViewHolder(
                ViewSeasonListUpNextBinding.inflate(layoutInflater, parent, false),
                timeDisplayFormatClicked
            )
            R.layout.view_season_list_button -> tmg.flashback.ui.dashboard.list.viewholders.ButtonViewHolder(
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
            is ListItem.Season -> (holder as tmg.flashback.ui.dashboard.list.viewholders.SeasonViewHolder).bind(item)
            is ListItem.Header -> (holder as tmg.flashback.ui.dashboard.list.viewholders.HeaderViewHolder).bind(item)
            is ListItem.UpNext -> (holder as tmg.flashback.ui.dashboard.list.viewholders.UpNextViewHolder).bind(item)
            is ListItem.Button -> (holder as tmg.flashback.ui.dashboard.list.viewholders.ButtonViewHolder).bind(item)
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