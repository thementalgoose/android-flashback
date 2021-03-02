package tmg.flashback.statistics.ui.overview.constructor.summary.viewholders

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.view_constructor_summary_header.view.*
import tmg.flashback.statistics.ui.overview.constructor.summary.ConstructorSummaryItem
import tmg.flashback.statistics.ui.shared.pill.PillAdapter
import tmg.flashback.statistics.ui.shared.pill.PillItem
import tmg.flashback.statistics.ui.util.getFlagResourceAlpha3
import tmg.utilities.extensions.views.context

class HeaderViewHolder(
        pillClicked: (PillItem) -> Unit,
        itemView: View
): RecyclerView.ViewHolder(itemView) {

    var adapter: PillAdapter = PillAdapter(pillClicked = pillClicked)

    init {
        itemView.links.adapter = adapter
        itemView.links.layoutManager = LinearLayoutManager(context).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }
    }

    fun bind(item: ConstructorSummaryItem.Header) {
        Glide.with(context)
                .load(context.getFlagResourceAlpha3(item.constructorNationalityISO))
                .into(itemView.nationality)

        itemView.colour.setBackgroundColor(item.constructorColor)

        adapter.list = mutableListOf(
                PillItem.Wikipedia(item.constructorWikiUrl)
        )
    }
}