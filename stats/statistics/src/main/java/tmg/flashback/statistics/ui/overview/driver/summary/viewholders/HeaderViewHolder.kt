package tmg.flashback.statistics.ui.overview.driver.summary.viewholders

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.statistics.ui.overview.driver.summary.DriverSummaryItem
import tmg.flashback.statistics.ui.shared.pill.PillAdapter
import tmg.flashback.statistics.ui.shared.pill.PillItem
import tmg.flashback.ui.extensions.getColor
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.ViewDriverSummaryHeaderBinding
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.utilities.extensions.views.context

class HeaderViewHolder(
        pillClicked: (PillItem) -> Unit,
        private val binding: ViewDriverSummaryHeaderBinding
): RecyclerView.ViewHolder(binding.root) {

    var adapter: PillAdapter = PillAdapter(pillClicked = pillClicked)

    init {
        binding.links.adapter = adapter
        binding.links.layoutManager = LinearLayoutManager(context).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }
    }

    fun bind(item: DriverSummaryItem.Header) {

        Glide.with(itemView.context)
                .load(item.driverImg)
                .into(binding.imgDriver)

        binding.tvNumber.text = item.driverNumber.toString()
        binding.tvNumber.colorHighlight = context.theme.getColor(R.attr.colorPrimary)
        binding.driverBirthday.text = itemView.context.getString(R.string.driver_overview_stat_birthday, item.driverBirthday.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")))

        binding.imgNationality.setImageResource(context.getFlagResourceAlpha3(item.driverNationalityISO))

        adapter.list = mutableListOf(
                PillItem.Wikipedia(item.driverWikiUrl)
        )
    }
}