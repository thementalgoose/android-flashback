package tmg.flashback.rss.ui.viewholder

import android.view.View
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_rss_item.view.*
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.repo.models.rss.Article
import tmg.flashback.rss.ui.RSSItem
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.views.show
import java.net.URL

class ItemViewHolder(
    val articleClicked: (link: Article, itemId: Long) -> Unit,
    itemView: View
) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private lateinit var item: Article
    private var expandableItemId: Long = -1L

    init {
        itemView.container.setOnClickListener(this)
    }

    fun bind(item: RSSItem.RSS, itemId: Long) {
        this.item = item.item
        this.expandableItemId = itemId

        val colour = item.item.source.colour.toColorInt()
        itemView.label.text = item.item.source.shortSource ?: item.item.source.title.substring(0, 2)
        itemView.label.setTextColor(item.item.source.textColor.toColorInt())
        itemView.imageBackground.setBackgroundColor(colour)

        itemView.title.text = item.item.title
        val shortDesc = item.item.description?.split("<br>")?.firstOrNull()
        itemView.description.show(!shortDesc.isNullOrEmpty())
        itemView.description.text = shortDesc?.fromHtml()
        itemView.source.text = item.item.url
        itemView.date.text = item.item.date?.format(DateTimeFormatter.ofPattern("HH:mm 'at' dd MMM")) ?: ""
        itemView.title.text = item.item.title
    }

    val Article.url: String
        get() {
            val url = URL(this.link)
            return "${url.protocol}://${url.host}"
        }

    override fun onClick(p0: View?) {
        articleClicked(item, expandableItemId)
    }
}