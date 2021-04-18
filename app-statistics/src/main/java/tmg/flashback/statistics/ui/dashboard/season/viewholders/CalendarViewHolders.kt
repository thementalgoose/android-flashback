package tmg.flashback.statistics.ui.dashboard.season.viewholders

import androidx.recyclerview.widget.RecyclerView
import org.threeten.bp.LocalDate
import org.threeten.bp.format.TextStyle
import tmg.flashback.statistics.databinding.LayoutDashboardSeasonCalendarWeekBinding
import tmg.flashback.statistics.databinding.ViewDashboardSeasonCalendarHeaderBinding
import tmg.flashback.statistics.databinding.ViewDashboardSeasonCalendarMonthBinding
import tmg.flashback.statistics.databinding.ViewDashboardSeasonCalendarWeekBinding
import tmg.flashback.statistics.ui.dashboard.season.SeasonItem
import tmg.utilities.extensions.views.show
import java.util.*

class CalendarHeaderViewHolder(
    private val binding: ViewDashboardSeasonCalendarHeaderBinding
): RecyclerView.ViewHolder(binding.root) {
}

class CalendarMonthViewHolder(
    private val binding: ViewDashboardSeasonCalendarMonthBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SeasonItem.CalendarMonth) {
        binding.month.text = item.month.getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault())
    }
}

class CalendarWeekViewHolder(
    private val binding: ViewDashboardSeasonCalendarWeekBinding
): RecyclerView.ViewHolder(binding.root) {

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

        cells.forEach {
            it.day.text = ""
        }

        val startingIndex = item.startingDay.dayOfWeek.value - 1

        val dayOfMonth = item.startingDay.dayOfMonth
        val lastDayOfMonth: Int = item.startingDay.withDayOfMonth(item.startingDay.lengthOfMonth()).dayOfMonth

        for ((offset, x) in (startingIndex until cells.size).withIndex()) {
            val day = (dayOfMonth + offset)
            cells[x].day.text = day.toString()
            cells[x].day.show(day <= lastDayOfMonth)
        }
    }
}