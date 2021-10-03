package tmg.flashback.statistics.ui.search.viewholder

import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.statistics.databinding.ViewSearchRaceBinding
import tmg.flashback.statistics.ui.search.SearchItem

class SearchRaceViewHolder(
    private val binding: ViewSearchRaceBinding,
    private val itemClicked: (item: SearchItem) -> Unit
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SearchItem.Race) {

    }
}