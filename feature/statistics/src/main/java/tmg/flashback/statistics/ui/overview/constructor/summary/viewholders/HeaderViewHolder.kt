package tmg.flashback.statistics.ui.overview.constructor.summary.viewholders

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tmg.flashback.statistics.databinding.ViewConstructorSummaryHeaderBinding
import tmg.flashback.statistics.ui.overview.constructor.summary.ConstructorSummaryItem
import tmg.flashback.statistics.ui.shared.pill.PillAdapter
import tmg.flashback.statistics.ui.shared.pill.PillItem
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.utilities.extensions.views.context

class HeaderViewHolder(
        pillClicked: (PillItem) -> Unit,
        private val binding: ViewConstructorSummaryHeaderBinding
): RecyclerView.ViewHolder(binding.root) {

    var adapter: PillAdapter = PillAdapter(pillClicked = pillClicked)

    init {
        binding.links.adapter = adapter
        binding.links.layoutManager = LinearLayoutManager(context).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }
    }

    fun bind(item: ConstructorSummaryItem.Header) {
        Glide.with(context)
                .load(context.getFlagResourceAlpha3(item.constructorNationalityISO))
                .into(binding.nationality)

        binding.colour.setBackgroundColor(item.constructorColor)

        adapter.list = mutableListOf(
                PillItem.Wikipedia(item.constructorWikiUrl)
        )
    }
}