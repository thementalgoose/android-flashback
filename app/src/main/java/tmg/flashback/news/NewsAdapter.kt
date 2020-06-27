package tmg.flashback.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.R
import tmg.flashback.home.list.HomeItem
import tmg.flashback.repo.models.news.Article
import tmg.flashback.shared.viewholders.InternalErrorOccurredViewHolder
import tmg.flashback.shared.viewholders.MessageViewHolder
import tmg.flashback.shared.viewholders.NoNetworkViewHolder
import tmg.flashback.shared.viewholders.NoNewsSourcesViewHolder

class NewsAdapter(
    val articleClicked: (item: Article, itemId: Long) -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    init {
        setHasStableIds(true)
    }

    var list: List<NewsItem> = emptyList()
        set(value) {
            val result = DiffUtil.calculateDiff(DiffCallback(field, value))
            field = value
            result.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.view_news_news -> NewsItemViewHolder(
                articleClicked,
                LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            )
            R.layout.view_shared_no_network -> NoNetworkViewHolder(
                    LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            )
            R.layout.view_shared_no_news_sources -> NoNewsSourcesViewHolder(
                LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            )
            R.layout.view_shared_internal_error -> InternalErrorOccurredViewHolder(
                LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            )
            R.layout.view_shared_message -> MessageViewHolder(
                LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            )
            else -> throw Exception("Type in NewsItem is not implemented on onCreateViewHolder")
        }
    }
    override fun getItemId(position: Int) = position.toLong()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = list[position]) {
            is NewsItem.News -> (holder as NewsItemViewHolder).bind(item, getItemId(position))
            is NewsItem.Message -> (holder as MessageViewHolder).bind(holder.itemView.context.getString(R.string.home_last_updated, item.msg))
        }
    }

    override fun getItemViewType(position: Int) = list[position].layoutId

    override fun getItemCount(): Int = list.size

    inner class DiffCallback(
        private val oldList: List<NewsItem>,
        private val newList: List<NewsItem>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(o: Int, n: Int) = oldList[o] == newList[n]

        override fun areContentsTheSame(o: Int, n: Int) = oldList[o] == newList[n]

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size
    }
}