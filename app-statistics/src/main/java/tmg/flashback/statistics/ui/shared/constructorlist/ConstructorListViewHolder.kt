package tmg.flashback.statistics.ui.shared.constructorlist

import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.data.models.stats.SlimConstructor
import tmg.flashback.statistics.databinding.ViewDriverConstructorsBinding

class ConstructorListViewHolder(
    private val binding: ViewDriverConstructorsBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: SlimConstructor) {
        binding.constructorColor.setBackgroundColor(item.color)
        binding.constructor.text = item.name
    }
}