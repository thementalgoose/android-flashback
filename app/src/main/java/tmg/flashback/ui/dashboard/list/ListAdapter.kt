package tmg.flashback.ui.dashboard.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.R
import tmg.flashback.ui.dashboard.list.viewholders.HeaderViewHolder
import tmg.flashback.ui.dashboard.list.viewholders.HeroViewHolder
import tmg.flashback.ui.dashboard.list.viewholders.SeasonViewHolder
import tmg.flashback.ui.dashboard.list.viewholders.UpNextViewHolder

class ListAdapter(
    val settingsClicked: () -> Unit,
    var featureToggled: (type: HeaderType) -> Unit,
    var favouriteToggled: (season: Int) -> Unit,
    var seasonClicked: (season: Int) -> Unit,
    var setDefaultClicked: (season: Int) -> Unit,
    var clearDefaultClicked: () -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var toggle: Boolean = false

    var list: List<ListItem> = emptyList()
        set(value) {
            val result = DiffUtil.calculateDiff(DiffCallback(field, value))
            field = value
            result.dispatchUpdatesTo(this)
        }

    fun setToggle(toggle: Boolean) {
        this.toggle = toggle
        List(list.size) {
            notifyItemChanged(it, true)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.view_season_list_season -> SeasonViewHolder(favouriteToggled, seasonClicked, setDefaultClicked, clearDefaultClicked, view)
            R.layout.view_season_list_header -> HeaderViewHolder(featureToggled, view)
            R.layout.view_season_list_hero -> HeroViewHolder(view, settingsClicked)
            R.layout.view_season_list_up_next -> UpNextViewHolder(view)
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
            return oldItem == newItem || itemsAreHeadersWithOnlyDifferentToggleState(oldItem, newItem)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        private fun itemsAreHeadersWithOnlyDifferentToggleState(oldItem: ListItem, newItem: ListItem): Boolean {
            return oldItem is ListItem.Header && newItem is ListItem.Header && oldItem.type == newItem.type
        }
    }
}