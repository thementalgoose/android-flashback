package tmg.flashback.regulations.ui.items.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.regulations.R
import tmg.flashback.regulations.databinding.ViewFormatCollapsibleBinding
import tmg.flashback.regulations.domain.Item
import tmg.utilities.extensions.views.getString
import tmg.utilities.extensions.views.show
import tmg.utilities.extensions.views.visible

internal class CollapsibleViewHolder(
    private val binding: ViewFormatCollapsibleBinding,
    private val setSection: (label: Int, isExpanded: Boolean) -> Unit
): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    private lateinit var item: Item.Collapsible

    init {
        binding.container.setOnClickListener(this)
    }

    fun bind(item: Item.Collapsible) {
        this.item = item

        binding.title.setText(item.label)
//        binding.arrow.show(item.expanded)
        when (item.expanded) {
            true -> {
                binding.title.contentDescription = getString(R.string.ab_section_expanded, getString(item.label))
//                binding.arrow.setImageResource(R.drawable.arrow_down)
//                binding.arrow.visible()
            }
            false -> {
                binding.title.contentDescription = getString(R.string.ab_section_collapsed, getString(item.label))
//                binding.arrow.setImageResource(R.drawable.arrow_up)
//                binding.arrow.visible()
            }
        }
    }

    override fun onClick(v: View?) {
        setSection(item.label, !item.expanded)
    }
}