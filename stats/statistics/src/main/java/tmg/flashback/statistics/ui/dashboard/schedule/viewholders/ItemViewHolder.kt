package tmg.flashback.statistics.ui.dashboard.schedule.viewholders

import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.statistics.databinding.ViewInlineScheduleItemBinding
import tmg.flashback.statistics.ui.dashboard.schedule.InlineSchedule
import tmg.utilities.extensions.format
import tmg.utilities.extensions.views.show

class ItemViewHolder(
    private val binding: ViewInlineScheduleItemBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: InlineSchedule.Item) {
        binding.title.text = item.label
        binding.time.text = item.time.format("HH:mm")
        binding.icon.show()
    }
}