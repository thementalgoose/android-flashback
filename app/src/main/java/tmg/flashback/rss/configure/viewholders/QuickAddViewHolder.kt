package tmg.flashback.rss.configure.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.repo.enums.SupportedArticleSource
import tmg.flashback.rss.configure.RSSConfigureItem

class QuickAddViewHolder(
    private val quickAddItem: (SupportedArticleSource) -> Unit,
    itemView: View
): RecyclerView.ViewHolder(itemView) {
    fun bind(item: RSSConfigureItem.QuickAdd) {

    }
}