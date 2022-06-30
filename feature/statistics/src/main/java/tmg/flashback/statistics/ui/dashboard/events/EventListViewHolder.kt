package tmg.flashback.statistics.ui.dashboard.events

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.formula1.extensions.icon
import tmg.flashback.formula1.model.Event
import tmg.flashback.statistics.databinding.ViewEventListItemBinding
import tmg.utilities.extensions.ordinalAbbreviation
import tmg.utilities.extensions.views.hide

class EventListViewHolder(
    private val binding: ViewEventListItemBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Event) {
        binding.icon.setImageResource(model.type.icon)
        binding.icon.hide()
        binding.label.text = model.label
        @SuppressLint("SetTextI18n")
        binding.date.text = "${model.date.dayOfMonth.ordinalAbbreviation} ${model.date.format(DateTimeFormatter.ofPattern("MMM yy"))}"
    }
}