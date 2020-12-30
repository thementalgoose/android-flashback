package tmg.flashback.dashboard.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.R
import tmg.flashback.dashboard.list.viewholders.HeaderViewHolder
import tmg.flashback.dashboard.list.viewholders.SeasonViewHolder
import tmg.flashback.dashboard.list.viewholders.TopViewHolder
import tmg.flashback.home.season.HeaderType
import tmg.flashback.home.season.SeasonListItem

private const val viewTypeSeason = 0
private const val viewTypeTop = 1
private const val viewTypeHeader = 2

class ListAdapter(
    var featureToggled: ((type: HeaderType) -> Unit)? = null,
    var favouriteToggled: ((season: Int) -> Unit)? = null,
    var seasonClicked: ((season: Int) -> Unit)? = null
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var toggle: Boolean = false

    var list: List<SeasonListItem> = emptyList()
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
        return when (viewType) {
            viewTypeSeason -> SeasonViewHolder(
                favouriteToggled,
                seasonClicked,
                LayoutInflater.from(parent.context).inflate(R.layout.view_season_list_season, parent, false)
            )
            viewTypeHeader -> HeaderViewHolder(
                featureToggled,
                LayoutInflater.from(parent.context).inflate(R.layout.view_season_list_header, parent, false)
            )
            viewTypeTop -> TopViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.view_season_list_top, parent, false)
            )
            else -> throw Exception("View type not implemented")
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        when (getItemViewType(position)) {
            viewTypeSeason -> (holder as SeasonViewHolder).bind(list[position] as SeasonListItem.Season, payloads.isNotEmpty(), toggle)
            viewTypeHeader -> (holder as HeaderViewHolder).bind(list[position] as SeasonListItem.Header, payloads.isNotEmpty(), toggle)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        this.onBindViewHolder(holder, position, mutableListOf())
    }

    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            is SeasonListItem.Season -> viewTypeSeason
            is SeasonListItem.Header -> viewTypeHeader
            SeasonListItem.Top -> viewTypeTop
        }
    }

    override fun getItemCount() = list.size

    inner class DiffCallback(
        private val oldList: List<SeasonListItem>,
        private val newList: List<SeasonListItem>
    ): DiffUtil.Callback() {
        override fun areItemsTheSame(old: Int, new: Int): Boolean {
            return oldList[old] == newList[new]
        }

        override fun areContentsTheSame(old: Int, new: Int): Boolean {
            return oldList[old] == newList[new]
        }

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size
    }
}