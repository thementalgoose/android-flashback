package tmg.flashback.rss.configure.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.rss.configure.RSSConfigureItem

class ItemViewHolder(
    private val removeItem: (String) -> Unit,
    itemView: View
): RecyclerView.ViewHolder(itemView) {
    fun bind(item: RSSConfigureItem.Item) {

    }
}