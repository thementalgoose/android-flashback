package tmg.flashback.upnext.ui.dashboard.viewholder

import androidx.recyclerview.widget.RecyclerView
import org.threeten.bp.LocalDate
import tmg.flashback.upnext.databinding.ViewBreakdownDayBinding
import tmg.flashback.upnext.ui.dashboard.UpNextBreakdownModel
import tmg.utilities.extensions.views.show

class DayViewHolder(
    private val binding: ViewBreakdownDayBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: UpNextBreakdownModel.Day) {
        binding.title.text = item.label
        binding.defaultIndicator.show(item.day == LocalDate.now())
    }
}