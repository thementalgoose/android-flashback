package tmg.flashback.statistics.ui.circuit.viewholders

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.threeten.bp.LocalDate
import tmg.flashback.statistics.R
import tmg.flashback.statistics.constants.Formula1.currentSeasonYear
import tmg.flashback.statistics.databinding.ViewCircuitInfoHeaderBinding
import tmg.flashback.statistics.ui.circuit.CircuitItem
import tmg.flashback.statistics.ui.shared.pill.PillAdapter
import tmg.flashback.statistics.ui.shared.pill.PillItem
import tmg.flashback.statistics.ui.util.getFlagResourceAlpha3
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.ordinalAbbreviation
import tmg.utilities.extensions.views.context
import tmg.utilities.extensions.views.getString

class HeaderViewHolder(
    private val clickShowOnMap: () -> Unit,
    private val clickWikipedia: () -> Unit,
    private val binding: ViewCircuitInfoHeaderBinding
): RecyclerView.ViewHolder(binding.root) {

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
        binding.links.adapter = linkAdapter
        binding.links.layoutManager = LinearLayoutManager(context).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }
    }

    fun bind(item: CircuitItem.CircuitInfo) {
        binding.imgCountry.setImageResource(context.getFlagResourceAlpha3(item.circuit.countryISO))
        binding.country.text = item.circuit.country
        val previouslyHosted = item.circuit.results.count { it.date <= LocalDate.now() }
        val minYear = item.circuit.results.minByOrNull { it.season }?.season
        val maxYear = item.circuit.results.maxByOrNull { it.season }?.season

        var subtitle = ""
        subtitle += if (minYear == null || maxYear == null) {
            getString(R.string.circuit_info_status, previouslyHosted)
        } else {
            getString(R.string.circuit_info_status_range, previouslyHosted, minYear, maxYear)
        }
        val thisYear = item.circuit.results.filter { it.date.year == currentSeasonYear }
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
        binding.status.text = subtitle.fromHtml()

        linkAdapter.list = mutableListOf<PillItem>().apply {
            item.circuit.wikiUrl?.let { add(PillItem.Wikipedia(it)) }
            add(PillItem.ShowOnMap())
        }

    }
}