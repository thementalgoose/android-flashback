package tmg.flashback.statistics.ui.dashboard.season.viewholders

import androidx.recyclerview.widget.RecyclerView
import org.threeten.bp.format.TextStyle
import tmg.flashback.statistics.databinding.ViewDashboardSeasonCalendarMonthBinding
import tmg.flashback.statistics.ui.dashboard.season.SeasonItem
import java.util.*

class CalendarMonthViewHolder(
    private val binding: ViewDashboardSeasonCalendarMonthBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SeasonItem.CalendarMonth) {
        binding.month.text =
            item.month.getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault())
    }
}