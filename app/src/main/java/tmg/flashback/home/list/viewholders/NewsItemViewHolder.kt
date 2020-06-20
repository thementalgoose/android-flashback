package tmg.flashback.home.list.viewholders

import android.view.View
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_home_news.view.*
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.home.list.HomeItem
import tmg.flashback.repo.models.news.Article
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.views.show

class NewsItemViewHolder(
    val articleClicked: (link: Article, itemId: Long) -> Unit,
    itemView: View
) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private lateinit var item: Article
    private var expandableItemId: Long = -1L

    init {
        itemView.container.setOnClickListener(this)
    }

    fun bind(item: HomeItem.NewsArticle, itemId: Long) {
        this.item = item.item
        this.expandableItemId = itemId

        val colour = item.item.source.colour.toColorInt()
        itemView.label.text = item.item.source.sourceShort
        itemView.label.setTextColor(item.item.source.textColor.toColorInt())
        itemView.imageBackground.setBackgroundColor(colour)

        itemView.title.text = item.item.title
        val shortDesc = item.item.description.split("<br>").firstOrNull()
        itemView.description.show(!shortDesc.isNullOrEmpty() && item.item.showDescription)
        itemView.description.text = shortDesc?.fromHtml()
        itemView.source.text = item.item.source.link
        itemView.date.text = item.item.date?.format(DateTimeFormatter.ofPattern("HH:mm 'at' dd MMM")) ?: ""
        itemView.title.text = item.item.title
    }

    override fun onClick(p0: View?) {
        articleClicked(item, expandableItemId)
    }
}