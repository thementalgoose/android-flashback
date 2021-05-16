package tmg.flashback.rss.ui.viewholder

import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.rss.databinding.ViewRssMessageBinding
import tmg.utilities.extensions.fromHtml

class MessageViewHolder(
    private val binding: ViewRssMessageBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: String) {
        binding.message.text = item.fromHtml()
    }
}