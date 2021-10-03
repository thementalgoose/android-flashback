package tmg.flashback.statistics.ui.search.viewholder

import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.statistics.databinding.ViewSearchConstructorBinding
import tmg.flashback.statistics.ui.search.SearchItem

class SearchConstructorViewHolder(
    private val binding: ViewSearchConstructorBinding,
    private val itemClicked: (item: SearchItem) -> Unit
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SearchItem.Constructor) {

    }
}