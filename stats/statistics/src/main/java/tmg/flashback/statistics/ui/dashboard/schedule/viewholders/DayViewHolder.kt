package tmg.flashback.statistics.ui.dashboard.schedule.viewholders

import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.statistics.databinding.ViewInlineScheduleDayBinding
import tmg.flashback.statistics.ui.dashboard.schedule.InlineSchedule
import tmg.utilities.extensions.format

class DayViewHolder(
    private val binding: ViewInlineScheduleDayBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: InlineSchedule.Day) {
        binding.title.text = item.localDate.format("EEEE d MMMM")
    }
}