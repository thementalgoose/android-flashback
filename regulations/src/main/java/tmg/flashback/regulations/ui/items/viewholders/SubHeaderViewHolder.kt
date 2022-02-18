package tmg.flashback.regulations.ui.items.viewholders

import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.regulations.databinding.ViewFormatSubheaderBinding
import tmg.flashback.regulations.domain.Item

internal class SubHeaderViewHolder(
    private val binding: ViewFormatSubheaderBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Item.SubHeader) {
        binding.title.setText(item.label)
    }
}