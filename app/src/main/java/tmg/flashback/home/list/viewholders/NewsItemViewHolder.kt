package tmg.flashback.home.list.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_home_news.view.*
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.home.list.HomeItem
import tmg.flashback.repo.models.news.NewsItem
import tmg.utilities.extensions.views.show

class NewsItemViewHolder(
    val articleClicked: (link: NewsItem, itemId: Long) -> Unit,
    itemView: View
): RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private lateinit var item: NewsItem
    private var expandableItemId: Long = -1L

    init {
        itemView.container.setOnClickListener(this)
    }

    fun bind(item: HomeItem.NewsArticle, itemId: Long) {
        this.item = item.item
        this.expandableItemId = itemId

        itemView.title.text = item.item.title
        itemView.description.show(item.item.description.isEmpty())
        itemView.description.text = item.item.description
        itemView.source.text = item.item.source.link
        itemView.date.text = item.item.date.format(DateTimeFormatter.ofPattern("HH:mm 'at' dd MMM"))
        itemView.title.text = item.item.title
    }

    override fun onClick(p0: View?) {
        articleClicked(item, expandableItemId)
    }
}