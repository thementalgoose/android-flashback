package tmg.flashback.rss.ui.settings.configure

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.rss.R
import tmg.flashback.rss.databinding.*
import tmg.flashback.rss.repo.model.SupportedArticleSource
import tmg.flashback.rss.ui.settings.configure.viewholders.*
import java.lang.RuntimeException

class RSSConfigureAdapter(
    private val customAddItem: (String) -> Unit,
    private val quickAddItem: (SupportedArticleSource) -> Unit,
    private val visitWebsite: (SupportedArticleSource) -> Unit,
    private val removeItem: (String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list: List<RSSConfigureItem> = emptyList()
        set(value) {
            val result = DiffUtil.calculateDiff(DiffCallback(field, value))
            field = value
            result.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.view_rss_configure_header -> HeaderViewHolder(
                ViewRssConfigureHeaderBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_rss_configure_item -> ItemViewHolder(
                removeItem,
                visitWebsite,
                ViewRssConfigureItemBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_rss_configure_quickadd -> QuickAddViewHolder(
                quickAddItem,
                visitWebsite,
                ViewRssConfigureQuickaddBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_rss_configure_no_items -> NoItemsViewHolder(
                ViewRssConfigureNoItemsBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_rss_configure_add -> AddViewHolder(
                customAddItem,
                ViewRssConfigureAddBinding.inflate(layoutInflater, parent, false)
            )
            else -> throw RuntimeException("ViewType not supported by configuration adapter")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = list[position]) {
            is RSSConfigureItem.Header -> (holder as HeaderViewHolder).bind(item)
            is RSSConfigureItem.Item -> (holder as ItemViewHolder).bind(item)
            is RSSConfigureItem.QuickAdd -> (holder as QuickAddViewHolder).bind(item)
        }
    }

    override fun getItemViewType(position: Int) = list[position].layoutId

    override fun getItemCount() = list.size

    inner class DiffCallback(
        private val old: List<RSSConfigureItem>,
        private val new: List<RSSConfigureItem>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            old[oldItemPosition] == new[newItemPosition]

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            old[oldItemPosition] == new[newItemPosition]

        override fun getNewListSize() = new.size
        override fun getOldListSize() = old.size
    }
}