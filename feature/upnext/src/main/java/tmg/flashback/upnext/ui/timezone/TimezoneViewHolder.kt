package tmg.flashback.upnext.ui.timezone

import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.upnext.databinding.ViewTimezoneBinding

class TimezoneViewHolder(
    private val binding: ViewTimezoneBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(timezoneItem: TimezoneItem) {
        binding.title.text = timezoneItem.label
    }
}