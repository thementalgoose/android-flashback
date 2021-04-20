package tmg.flashback.statistics.ui.dashboard.season.viewholders

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.threeten.bp.DayOfWeek
import org.threeten.bp.format.TextStyle
import tmg.flashback.statistics.databinding.ViewDashboardSeasonCalendarHeaderBinding
import java.util.*

class CalendarHeaderViewHolder(
    private val binding: ViewDashboardSeasonCalendarHeaderBinding
) : RecyclerView.ViewHolder(binding.root) {

    val cells: List<TextView> by lazy {
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

    init {
        DayOfWeek.values().forEachIndexed { index, dayOfWeek ->
            cells[index].text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        }
    }
}