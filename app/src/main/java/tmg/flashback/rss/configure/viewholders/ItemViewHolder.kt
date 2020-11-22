package tmg.flashback.rss.configure.viewholders

import android.annotation.SuppressLint
import android.content.ClipData
import android.view.View
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_rss_configure_item.view.*
import tmg.flashback.R
import tmg.flashback.rss.configure.RSSConfigureItem
import tmg.flashback.utils.getColor
import tmg.utilities.extensions.views.context
import java.net.URL

class ItemViewHolder(
    private val removeItem: (String) -> Unit,
    itemView: View
): RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private lateinit var item: RSSConfigureItem.Item

    init {
        itemView.remove.setOnClickListener(this)
    }

    fun bind(item: RSSConfigureItem.Item) {
        this.item = item
        if (item.supportedArticleSource != null) {
            itemView.imageBackground.setBackgroundColor(item.supportedArticleSource.colour.toColorInt())
            itemView.label.text = item.supportedArticleSource.sourceShort
            itemView.label.setTextColor(item.supportedArticleSource.textColour.toColorInt())
            itemView.title.text = item.supportedArticleSource.source
            itemView.description.text = item.supportedArticleSource.rssLink
        }
        else {
            itemView.imageBackground.setBackgroundColor(context.theme.getColor(R.attr.colorPrimary))
            itemView.label.text = "..."

            val url = URL(item.url)
            @SuppressLint("SetTextI18n")
            itemView.title.text = "${url.protocol}://${url.host}"

            itemView.description.text = item.url
        }
    }

    override fun onClick(p0: View?) {
        removeItem(item.supportedArticleSource?.rssLink ?: item.url)
    }
}