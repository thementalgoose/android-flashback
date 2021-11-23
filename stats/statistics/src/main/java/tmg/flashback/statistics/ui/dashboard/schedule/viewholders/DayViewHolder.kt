package tmg.flashback.statistics.ui.dashboard.schedule.viewholders

import androidx.recyclerview.widget.RecyclerView
import org.threeten.bp.LocalDate
import tmg.flashback.statistics.databinding.ViewInlineScheduleDayBinding
import tmg.flashback.statistics.ui.dashboard.schedule.InlineSchedule
import tmg.utilities.extensions.format
import tmg.utilities.extensions.views.show

class DayViewHolder(
    private val binding: ViewInlineScheduleDayBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: InlineSchedule.Day) {
        binding.defaultIndicator.show(item.localDate == LocalDate.now(), isGone = false)
        binding.title.text = item.localDate.format("EEEE d MMMM")
    }
}