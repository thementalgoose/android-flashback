package tmg.flashback.statistics.ui.dashboard.season.viewholders

import androidx.recyclerview.widget.RecyclerView
import org.threeten.bp.LocalDate
import tmg.flashback.statistics.databinding.LayoutDashboardSeasonCalendarWeekBinding
import tmg.flashback.statistics.databinding.ViewDashboardSeasonCalendarWeekBinding
import tmg.flashback.statistics.ui.dashboard.season.SeasonItem
import tmg.flashback.statistics.ui.util.getFlagResourceAlpha3
import tmg.utilities.extensions.views.context
import tmg.utilities.extensions.views.show

class CalendarWeekViewHolder(
    private val binding: ViewDashboardSeasonCalendarWeekBinding
) : RecyclerView.ViewHolder(binding.root) {

    val cells: List<LayoutDashboardSeasonCalendarWeekBinding> by lazy {
        return@lazy listOf(
            binding.cell1,
            binding.cell2,
            binding.cell3,
            binding.cell4,
            binding.cell5,
            binding.cell6,
            binding.cell7
        )
    }

    fun bind(item: SeasonItem.CalendarWeek) {

        binding.highlight.show(item.race != null)

        cells.forEach {
            it.day.text = ""
        }

        val startingIndex = item.startingDay.dayOfWeek.value - 1

        val dayOfMonth = item.startingDay.dayOfMonth
        val lastDayOfMonth: LocalDate =
            item.startingDay.withDayOfMonth(item.startingDay.lengthOfMonth())

        var lastSimulatedDay = 0

        for ((offset, x) in (startingIndex until cells.size).withIndex()) {

            val day = (dayOfMonth + offset)
            cells[x].day.text = day.toString()
            cells[x].day.show(day <= lastDayOfMonth.dayOfMonth)

            val date = item.startingDay.plusDays(offset.toLong())
            if (LocalDate.now() > date) {
                cells[x].day.alpha = textAlpha
            } else {
                cells[x].day.alpha = 1.0f
            }

            lastSimulatedDay = day
        }

        if (lastSimulatedDay > lastDayOfMonth.dayOfMonth || item.race == null) {
            binding.highlight.alpha = alphaHighlightEnabled
            binding.highlight.show(false)
            binding.flag.show(false)
        } else {
            if (item.race.date < LocalDate.now()) {
                binding.highlight.alpha = alphaHighlightDisabled
            }
            else {
                binding.highlight.alpha = alphaHighlightEnabled
            }
            binding.highlight.show(true)
            binding.flag.show(true)
            binding.flag.setImageResource(context.getFlagResourceAlpha3(item.race.countryISO))
        }
    }

    companion object {
        private const val alphaHighlightEnabled = 0.6f
        private const val alphaHighlightDisabled = 0.2f

        private const val textAlpha = 0.35f
    }
}