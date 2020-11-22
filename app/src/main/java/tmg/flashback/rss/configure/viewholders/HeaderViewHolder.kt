package tmg.flashback.rss.configure.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_rss_configure_header.view.*
import tmg.flashback.rss.configure.RSSConfigureItem

class HeaderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    fun bind(item: RSSConfigureItem.Header) {
        itemView.header.setText(item.text)
    }
}