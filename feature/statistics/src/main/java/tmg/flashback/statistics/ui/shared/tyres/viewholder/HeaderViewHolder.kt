package tmg.flashback.statistics.ui.shared.tyres.viewholder

import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.statistics.databinding.ViewTyreHeaderBinding
import tmg.flashback.statistics.databinding.ViewTyreItemBinding
import tmg.flashback.statistics.ui.shared.tyres.TyreItem

class HeaderViewHolder(
    private val binding: ViewTyreHeaderBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: TyreItem.Header) {
        binding.header.setText(item.label)
    }
}