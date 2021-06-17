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
        binding.title.text = item.label
        item.item.ifDateAndTime { utc, local ->
            binding.time.text = local.format(DateTimeFormatter.ofPattern("HH:mm"))
        }
        item.item.ifDate { local ->
            binding.time.text = context.getString(R.string.dashboard_up_next_all_day)
        }
    }
}