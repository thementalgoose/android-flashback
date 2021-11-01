package tmg.flashback.statistics.ui.shared.constructorlist

import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.SlimConstructor
import tmg.flashback.statistics.databinding.ViewDriverConstructorsBinding

class ConstructorListViewHolder(
    private val binding: ViewDriverConstructorsBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Constructor) {
        binding.constructorColor.setBackgroundColor(item.color)
        binding.constructor.text = item.name
    }
}