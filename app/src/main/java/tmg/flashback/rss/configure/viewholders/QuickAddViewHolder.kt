package tmg.flashback.rss.configure.viewholders

import android.view.View
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_rss_configure_quickadd.view.*
import tmg.flashback.R
import tmg.flashback.repo.enums.SupportedArticleSource
import tmg.flashback.rss.configure.RSSConfigureItem

class QuickAddViewHolder(
    private val quickAddItem: (SupportedArticleSource) -> Unit,
    itemView: View
): RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private lateinit var item: SupportedArticleSource

    init {
        itemView.add.setOnClickListener(this)
    }

    fun bind(item: RSSConfigureItem.QuickAdd) {

        itemView.alpha = 0.8f

        this.item = item.supportedArticleSource
        itemView.imageBackground.setBackgroundColor(item.supportedArticleSource.colour.toColorInt())
        itemView.label.text = item.supportedArticleSource.sourceShort
        itemView.label.setTextColor(item.supportedArticleSource.textColour.toColorInt())
        itemView.title.text = item.supportedArticleSource.source
        itemView.description.text = item.supportedArticleSource.rssLink
    }

    override fun onClick(p0: View?) {
        quickAddItem(item)
    }
}