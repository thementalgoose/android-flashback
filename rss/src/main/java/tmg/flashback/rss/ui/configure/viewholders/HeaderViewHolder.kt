package tmg.flashback.rss.ui.configure.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_rss_configure_header.view.*
import tmg.flashback.rss.R
import tmg.flashback.rss.ui.configure.RSSConfigureItem
import tmg.utilities.extensions.views.show

class HeaderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    fun bind(item: RSSConfigureItem.Header) {

        if (item.text == R.string.rss_configure_header_quick_add) {
            itemView.alpha = 0.8f
        }
        else {
            itemView.alpha = 1.0f
        }

        itemView.header.setText(item.text)

        itemView.subtitle.show(item.subtitle != null)
        item.subtitle?.let {
            itemView.subtitle.setText(it)
        }
    }
}