package tmg.flashback.rss.ui.configure.viewholders

import android.annotation.SuppressLint
import android.view.View
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_rss_configure_item.view.*
import tmg.flashback.rss.R
import tmg.flashback.rss.repo.model.SupportedArticleSource
import tmg.flashback.rss.ui.configure.RSSConfigureItem
import tmg.utilities.extensions.getColor
import tmg.utilities.extensions.views.context
import tmg.utilities.extensions.views.gone
import tmg.utilities.extensions.views.show
import java.net.MalformedURLException
import java.net.URL

class ItemViewHolder(
    private val removeItem: (String) -> Unit,
    private val visitWebsite: (SupportedArticleSource) -> Unit,
    itemView: View
): RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private lateinit var item: RSSConfigureItem.Item

    init {
        itemView.remove.setOnClickListener(this)
        itemView.visit_website.setOnClickListener(this)
    }

    fun bind(item: RSSConfigureItem.Item) {
        this.item = item
        itemView.alpha = 1.0f
        if (item.supportedArticleSource != null) {
            itemView.imageBackground.setBackgroundColor(item.supportedArticleSource.colour.toColorInt())
            itemView.label.text = item.supportedArticleSource.sourceShort
            itemView.label.setTextColor(item.supportedArticleSource.textColour.toColorInt())
            itemView.title.text = item.supportedArticleSource.source
            itemView.description.text = item.supportedArticleSource.rssLink
            itemView.visit_website.show()
        }
        else {
            itemView.imageBackground.setBackgroundColor(context.theme.getColor(R.attr.rssBrandPrimary))
            itemView.label.text = "..."
            itemView.visit_website.gone()

            try {
                val url = URL(item.url)
                @SuppressLint("SetTextI18n")
                itemView.title.text = "${url.protocol}://${url.host}"
                itemView.description.text = item.url
            } catch (e: MalformedURLException) {
                itemView.title.text = item.url
                itemView.description.setText(R.string.rss_configure_malformed_url)
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0) {
            itemView.remove -> {
                removeItem(item.supportedArticleSource?.rssLink ?: item.url)
            }
            itemView.visit_website -> {
                this.item.supportedArticleSource?.let {
                    visitWebsite(it)
                }
            }
        }
    }
}