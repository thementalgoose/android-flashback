package tmg.flashback.regulations.ui.items.viewholders

import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.regulations.databinding.ViewFormatTextBinding
import tmg.flashback.regulations.domain.Item

internal class TextViewHolder(
    private val binding: ViewFormatTextBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Item.Text) {
        binding.label.setText(item.label)
    }
}