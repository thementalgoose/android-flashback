package tmg.flashback.ui.overviews.driver.summary.viewholders

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.view_driver_summary_header.view.*
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.R
import tmg.flashback.ui.overviews.driver.summary.DriverSummaryItem
import tmg.flashback.ui.shared.pill.PillAdapter
import tmg.flashback.ui.shared.pill.PillItem
import tmg.flashback.ui.utils.getColor
import tmg.flashback.ui.utils.getFlagResourceAlpha3
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

    fun bind(item: DriverSummaryItem.Header) {

        Glide.with(itemView.context)
                .load(item.driverImg)
                .into(itemView.imgDriver)

        itemView.tvNumber.text = item.driverNumber.toString()
        itemView.tvNumber.colorHighlight = context.theme.getColor(R.attr.colorPrimary)
        itemView.driverBirthday.text = itemView.context.getString(R.string.driver_overview_stat_birthday, item.driverBirthday.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")))

        itemView.imgNationality.setImageResource(context.getFlagResourceAlpha3(item.driverNationalityISO))

        adapter.list = mutableListOf(
                PillItem.Wikipedia(item.driverWikiUrl)
        )
    }
}