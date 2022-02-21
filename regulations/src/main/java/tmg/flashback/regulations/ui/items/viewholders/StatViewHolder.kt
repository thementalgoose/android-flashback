package tmg.flashback.regulations.ui.items.viewholders

import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.regulations.databinding.ViewFormatStatBinding
import tmg.flashback.regulations.domain.Item
import tmg.utilities.extensions.views.context

internal class StatViewHolder(
    private val binding: ViewFormatStatBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Item.Stat) {

        binding.value.text = item.value.resolve(context)
        binding.icon.setImageResource(item.icon)
        binding.label.setText(item.label)
    }
}