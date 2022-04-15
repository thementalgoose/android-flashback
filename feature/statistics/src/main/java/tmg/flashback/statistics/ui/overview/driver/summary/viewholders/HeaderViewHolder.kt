package tmg.flashback.statistics.ui.overview.driver.summary.viewholders

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.ViewDriverSummaryHeaderBinding
import tmg.flashback.statistics.ui.overview.driver.summary.DriverSummaryItem
import tmg.flashback.statistics.ui.shared.pill.PillAdapter
import tmg.flashback.statistics.ui.shared.pill.PillItem
import tmg.flashback.ui.animation.GlideProvider
import tmg.flashback.ui.extensions.getColor
import tmg.utilities.extensions.views.context

private val glideProvider: tmg.flashback.ui.animation.GlideProvider =
    tmg.flashback.ui.animation.GlideProvider()

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

        glideProvider.load(binding.imgDriver, item.driverImg)

        binding.tvNumber.text = item.driverNumber?.toString() ?: ""
        binding.tvNumber.colorHighlight = when (item.constructors.isNotEmpty()) {
            true -> item.constructors.last().color
            false -> context.theme.getColor(R.attr.colorPrimary)
        }

        binding.driverBirthday.text = itemView.context.getString(R.string.driver_overview_stat_birthday, item.driverBirthday.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")))
        binding.imgNationality.setImageResource(context.getFlagResourceAlpha3(item.driverNationalityISO))

        adapter.list = mutableListOf(
                PillItem.Wikipedia(item.driverWikiUrl)
        )
    }
}