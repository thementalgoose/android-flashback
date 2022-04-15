package tmg.flashback.statistics.ui.shared.tyres.viewholder

import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.ViewTyreItemBinding
import tmg.flashback.statistics.ui.shared.tyres.TyreItem
import tmg.utilities.extensions.views.getString

class ItemViewHolder(
    private val binding: ViewTyreItemBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: TyreItem.Item) {
        binding.tyre.setImageResource(item.tyreLabel.tyre.icon)
        binding.label.setText(item.tyreLabel.label)
        binding.size.text = getString(R.string.tyres_size, item.tyreLabel.tyre.size)
    }
}