package tmg.flashback.upnext.ui.dashboard.viewholder

import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.upnext.databinding.ViewBreakdownItemBinding
import tmg.flashback.upnext.ui.dashboard.UpNextBreakdownModel

class ItemViewHolder(
    private val binding: ViewBreakdownItemBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: UpNextBreakdownModel.Item) {
        binding.title.text = item.label
    }
}