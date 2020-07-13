package tmg.flashback.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.R
import tmg.flashback.repo.models.news.Article
import tmg.flashback.shared.SyncAdapter
import tmg.flashback.shared.viewholders.InternalErrorOccurredViewHolder
import tmg.flashback.shared.viewholders.MessageViewHolder
import tmg.flashback.shared.viewholders.NoNetworkViewHolder
import tmg.flashback.shared.viewholders.NoNewsSourcesViewHolder
import tmg.flashback.utils.calculateDiff

class NewsAdapter(
    val articleClicked: (item: Article, itemId: Long) -> Unit
): SyncAdapter<NewsItem>() {

    init {
        setHasStableIds(true)
    }

    override var list: List<NewsItem> = emptyList()
        set(value) {
            val result = calculateDiff(field, value)
            field = value
            result.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.view_news_news -> NewsItemViewHolder(
                articleClicked,
                LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            )
            else -> super.onCreateViewHolder(parent, viewType)
        }
    }
    override fun getItemId(position: Int) = position.toLong()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = list[position]) {
            is NewsItem.News -> (holder as NewsItemViewHolder).bind(item, getItemId(position))
            is NewsItem.Message -> (holder as MessageViewHolder).bind(holder.itemView.context.getString(R.string.home_last_updated, item.msg))
        }
    }

    override fun viewType(position: Int) = list[position].layoutId
}