package tmg.flashback.statistics.ui.shared.schedule.viewholders

import androidx.recyclerview.widget.RecyclerView
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import tmg.flashback.statistics.databinding.ViewInlineScheduleItemBinding
import tmg.flashback.statistics.ui.shared.schedule.InlineSchedule
import tmg.utilities.extensions.format
import tmg.utilities.extensions.views.show

class ItemViewHolder(
    private val binding: ViewInlineScheduleItemBinding
): RecyclerView.ViewHolder(binding.root) {

    private lateinit var item: InlineSchedule.Item

    fun bind(item: InlineSchedule.Item) {
        this.item = item
        binding.title.text = item.label
        binding.time.text = item.time.format("HH:mm")
        binding.icon.show(item.showBell, isGone = false)

        updateAlpha()
    }

    private fun updateAlpha() {
        when {
            item.date < LocalDate.now() -> fade()
            item.date == LocalDate.now() -> {
                if (item.time < LocalTime.now()) {
                    fade()
                }
                else {
                    unfade()
                }
            }
            else -> unfade()
        }
    }

    private fun fade() { binding.container.alpha = 0.5f }
    private fun unfade() { binding.container.alpha = 1.0f }
}