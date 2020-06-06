package tmg.flashback.home.season

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.R
import tmg.flashback.home.season.viewholders.HeaderViewHolder
import tmg.flashback.home.season.viewholders.SeasonViewHolder

private const val viewTypeSeason = 0
private const val viewTypeHeader = 2

class SeasonListAdapter(
    var featureToggled: ((type: HeaderType) -> Unit)? = null,
    var favouriteToggled: ((season: Int) -> Unit)? = null,
    var seasonClicked: ((season: Int) -> Unit)? = null
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list: List<SeasonListItem> = emptyList()
        set(value) {
            val result = DiffUtil.calculateDiff(DiffCallback(field, value))
            field = value
            result.dispatchUpdatesTo(this)
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
            else -> throw Exception("View type not implemented")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            viewTypeSeason -> (holder as SeasonViewHolder).bind(list[position] as SeasonListItem.Season)
            viewTypeHeader -> (holder as HeaderViewHolder).bind(list[position] as SeasonListItem.Header)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            is SeasonListItem.Season -> viewTypeSeason
            is SeasonListItem.Header -> viewTypeHeader
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