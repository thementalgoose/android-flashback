package tmg.flashback.regulations.ui.items.viewholders

import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.regulations.databinding.ViewFormatHeaderBinding
import tmg.flashback.regulations.domain.Item

internal class HeaderViewHolder(
    private val binding: ViewFormatHeaderBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Item.Header) {
        binding.title.setText(item.label)
    }
}