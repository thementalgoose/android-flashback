package tmg.flashback.statistics.ui.search.viewholder

import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.statistics.databinding.ViewSearchCircuitBinding
import tmg.flashback.statistics.ui.search.SearchItem

class SearchCircuitViewHolder(
    private val binding: ViewSearchCircuitBinding,
    private val itemClicked: (item: SearchItem) -> Unit
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SearchItem.Circuit) {

    }
}