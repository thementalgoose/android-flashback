package tmg.flashback.upnext.ui.dashboard.viewholder

import androidx.recyclerview.widget.RecyclerView
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.upnext.R
import tmg.flashback.upnext.databinding.ViewBreakdownItemBinding
import tmg.flashback.upnext.ui.dashboard.UpNextBreakdownModel
import tmg.utilities.extensions.views.context

class ItemViewHolder(
    private val binding: ViewBreakdownItemBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: UpNextBreakdownModel.Item) {
        when (item.isInPast) {
            false -> {
                binding.title.alpha = 1.0f
                binding.time.alpha = 1.0f
            }
            true -> {
                binding.title.alpha = 0.4f
                binding.time.alpha = 0.4f
            }
        }

        binding.title.text = item.label
        item.item.on(
            dateAndTime = { utc, local ->
                binding.time.text = local.format(DateTimeFormatter.ofPattern("HH:mm"))
            },
            dateOnly = {
                binding.time.text = context.getString(R.string.dashboard_up_next_all_day)
            }
        )
    }
}