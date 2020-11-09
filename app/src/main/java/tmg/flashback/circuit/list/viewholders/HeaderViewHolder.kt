package tmg.flashback.circuit.list.viewholders

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_circuit_info_header.view.*
import org.threeten.bp.LocalDate
import tmg.flashback.R
import tmg.flashback.circuit.list.CircuitItem
import tmg.flashback.currentYear
import tmg.flashback.shared.pill.PillAdapter
import tmg.flashback.shared.pill.PillItem
import tmg.flashback.utils.getFlagResourceAlpha3
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.ordinalAbbreviation
import tmg.utilities.extensions.views.context
import tmg.utilities.extensions.views.getString
import tmg.utilities.extensions.views.show

class HeaderViewHolder(
    private val clickShowOnMap: () -> Unit,
    private val clickWikipedia: () -> Unit,
    itemView: View
): RecyclerView.ViewHolder(itemView) {

    private var linkAdapter = PillAdapter(
            pillClicked = {
                when (it) {
                    is PillItem.Wikipedia -> clickWikipedia()
                    is PillItem.ShowOnMap -> clickShowOnMap()
                    else -> {} /* Do nothing */
                }
            }
    )

    init {
        itemView.links.adapter = linkAdapter
        itemView.links.layoutManager = LinearLayoutManager(context).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }
    }

    fun bind(item: CircuitItem.CircuitInfo) {
        itemView.imgCountry.setImageResource(context.getFlagResourceAlpha3(item.circuit.countryISO))
        itemView.country.text = item.circuit.country
        val previouslyHosted = item.circuit.results.count { it.date <= LocalDate.now() }
        val minYear = item.circuit.results.minBy { it.season }?.season
        val maxYear = item.circuit.results.maxBy { it.season }?.season

        var subtitle = ""
        subtitle += if (minYear == null || maxYear == null) {
            getString(R.string.circuit_info_status, previouslyHosted)
        } else {
            getString(R.string.circuit_info_status_range, previouslyHosted, minYear, maxYear)
        }
        val thisYear = item.circuit.results.filter { it.date.year == currentYear }
        if (thisYear.isNotEmpty()) {
            subtitle += if (thisYear.size == 1) {
                "<br/>${getString(R.string.circuit_info_status_this_year_single, thisYear[0].round.ordinalAbbreviation)}"
            } else {
                val roundList = thisYear.map { "<b>${it.round.ordinalAbbreviation}</b>" }
                var result = ""
                roundList.forEachIndexed { index, value ->
                    result += value
                    result += when (index) {
                        roundList.size - 2 -> " and "
                        roundList.size - 1 -> ""
                        else -> ", "
                    }
                }
                "<br/>${getString(R.string.circuit_info_status_this_year_multiple, result)}"
            }
        }
        itemView.status.text = subtitle.fromHtml()

        linkAdapter.list = mutableListOf<PillItem>().apply {
            item.circuit.wikiUrl?.let { add(PillItem.Wikipedia(it)) }
            add(PillItem.ShowOnMap())
        }

    }
}