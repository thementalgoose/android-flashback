package tmg.flashback.regulations.ui.items.viewholders

import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.regulations.databinding.ViewFormatLedToBinding
import tmg.flashback.regulations.domain.Item

internal class DeterminesViewHolder(
    private val binding: ViewFormatLedToBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Item.Determines) {
        binding.from.setText(item.from)
        binding.to.setText(item.to)
    }
}