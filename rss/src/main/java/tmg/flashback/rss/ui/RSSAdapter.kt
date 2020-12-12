package tmg.flashback.rss.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.calculateDiff
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.rss.R
import tmg.flashback.rss.repo.model.Article
import tmg.flashback.rss.ui.viewholder.*
import java.lang.RuntimeException

class RSSAdapter(
        private val articleClicked: (item: Article, itemId: Long) -> Unit,
        private val openConfigure: () -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list: List<RSSItem> = emptyList()
        set(value) {
            val result = calculateDiff(DiffCallback(field, value))
            field = value
            result.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.view_rss_item -> ItemViewHolder(articleClicked, view)
            R.layout.view_rss_sources_disabled -> SourcesDisabledViewHolder(openConfigure, view)
            R.layout.view_rss_message -> MessageViewHolder(view)
            R.layout.view_rss_internal_error -> InternalErrorViewHolder(view)
            R.layout.view_rss_no_network -> NoNetworkViewHolder(view)
            else -> throw RuntimeException("View not supported in RSS feed")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = list[position]) {
            is RSSItem.RSS -> (holder as ItemViewHolder).bind(item, getItemId(position))
            is RSSItem.Message -> (holder as MessageViewHolder).bind(holder.itemView.context.getString(
                R.string.home_last_updated, item.msg))
        }
    }

    override fun getItemViewType(position: Int) = list[position].layoutId

    override fun getItemCount() = list.size

    inner class DiffCallback(
        private val old: List<RSSItem>,
        private val new: List<RSSItem>
    ): DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = old[oldItemPosition] == new[newItemPosition]

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = old[oldItemPosition] == new[newItemPosition]

        override fun getOldListSize(): Int = old.size

        override fun getNewListSize(): Int = new.size
    }
}