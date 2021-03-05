package tmg.flashback.statistics.ui.shared.pill

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.statistics.databinding.ViewLinkPillBinding

class PillViewHolder(
        val linkClicked: (PillItem) -> Unit,
        private val binding: ViewLinkPillBinding
): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    lateinit var item: PillItem

    init {
        binding.container.setOnClickListener(this)
    }

    fun bind(item: PillItem) {
        this.item = item
        binding.text.setText(item.label)
        binding.image.setImageResource(item.icon)
    }

    override fun onClick(p0: View?) {
        linkClicked(item)
    }
}