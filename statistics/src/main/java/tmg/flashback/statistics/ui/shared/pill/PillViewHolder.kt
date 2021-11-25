package tmg.flashback.statistics.ui.shared.pill

import android.graphics.Typeface
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.statistics.databinding.ViewLinkPillBinding
import tmg.utilities.extensions.views.context
import tmg.utilities.extensions.views.show

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

        binding.text.text = item.label.resolve(context)
        val typeface = if (item.highlighted) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
        binding.text.typeface = typeface

        binding.image.show(item.icon != null)
        binding.image.setImageResource(item.icon ?: 0)
    }

    override fun onClick(p0: View?) {
        linkClicked(item)
    }
}