package tmg.flashback.dashboard.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.R
import tmg.flashback.dashboard.list.viewholders.HeaderViewHolder
import tmg.flashback.dashboard.list.viewholders.HeroViewHolder
import tmg.flashback.dashboard.list.viewholders.SeasonViewHolder
import tmg.flashback.dashboard.list.viewholders.TopViewHolder
import tmg.flashback.utils.GenericDiffCallback

class ListAdapter(
    val settingsClicked: () -> Unit,
    var featureToggled: ((type: HeaderType) -> Unit)? = null,
    var favouriteToggled: ((season: Int) -> Unit)? = null,
    var seasonClicked: ((season: Int) -> Unit)? = null
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
            R.layout.view_season_list_season -> SeasonViewHolder(favouriteToggled, seasonClicked, view)
            R.layout.view_season_list_header -> HeaderViewHolder(featureToggled, view)
            R.layout.view_season_list_top -> TopViewHolder(view)
            R.layout.view_season_list_hero -> HeroViewHolder(view, settingsClicked)
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