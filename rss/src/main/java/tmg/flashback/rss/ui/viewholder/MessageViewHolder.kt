package tmg.flashback.rss.ui.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_rss_message.view.*
import tmg.flashback.rss.ui.RSSItem

class MessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    fun bind(item: String) {
        itemView.message.text = item
    }
}