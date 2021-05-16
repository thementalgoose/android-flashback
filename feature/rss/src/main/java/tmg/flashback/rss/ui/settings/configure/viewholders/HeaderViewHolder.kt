package tmg.flashback.rss.ui.settings.configure.viewholders

import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.rss.R
import tmg.flashback.rss.databinding.ViewRssConfigureHeaderBinding
import tmg.flashback.rss.ui.settings.configure.RSSConfigureItem
import tmg.utilities.extensions.views.show

class HeaderViewHolder(
    private val binding: ViewRssConfigureHeaderBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: RSSConfigureItem.Header) {

        if (item.text == R.string.rss_configure_header_quick_add) {
            binding.root.alpha = 0.8f
        }
        else {
            binding.root.alpha = 1.0f
        }

        binding.header.setText(item.text)

        binding.subtitle.show(item.subtitle != null)
        item.subtitle?.let {
            binding.subtitle.setText(it)
        }
    }
}