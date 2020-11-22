package tmg.flashback.rss

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.R
import tmg.flashback.repo.models.rss.Article
import tmg.flashback.rss.viewholder.RSSItemViewHolder
import tmg.flashback.rss.viewholder.RSSSourcesDisabledViewHolder
import tmg.flashback.shared.sync.SyncAdapter
import tmg.flashback.shared.viewholders.MessageViewHolder
import tmg.flashback.utils.calculateDiff

class RSSAdapter(
    private val articleClicked: (item: Article, itemId: Long) -> Unit,
    private val openConfigure: () -> Unit
): SyncAdapter<RSSItem>() {

    init {
        setHasStableIds(true)
    }

    override var list: List<RSSItem> = emptyList()
        set(value) {
            val result = calculateDiff(field, value)
            field = value
            result.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.view_rss_item -> RSSItemViewHolder(
                articleClicked,
                LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            )
            R.layout.view_rss_sources_disabled -> RSSSourcesDisabledViewHolder(
                openConfigure,
                LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            )
            else -> super.onCreateViewHolder(parent, viewType)
        }
    }
    override fun getItemId(position: Int) = position.toLong()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = list[position]) {
            is RSSItem.RSS -> (holder as RSSItemViewHolder).bind(item, getItemId(position))
            is RSSItem.Message -> (holder as MessageViewHolder).bind(holder.itemView.context.getString(R.string.home_last_updated, item.msg))
        }
    }

    override fun viewType(position: Int) = list[position].layoutId
}