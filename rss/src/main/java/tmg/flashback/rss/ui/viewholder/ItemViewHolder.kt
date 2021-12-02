package tmg.flashback.rss.ui.viewholder

import android.view.View
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.rss.databinding.ViewRssItemBinding
import tmg.flashback.rss.repo.model.Article
import tmg.flashback.rss.ui.RSSItem
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.views.show
import java.net.URL

class ItemViewHolder(
        val articleClicked: (link: Article, itemId: Long) -> Unit,
        private val binding: ViewRssItemBinding
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    private lateinit var item: Article
    private var expandableItemId: Long = -1L

    init {
        binding.container.setOnClickListener(this)
    }

    fun bind(item: RSSItem.RSS, itemId: Long) {
        this.item = item.item
        this.expandableItemId = itemId

        val colour = item.item.source.colour.toColorInt()
        binding.label.text = item.item.source.shortSource ?: item.item.source.title.substring(0, 2)
        binding.label.setTextColor(item.item.source.textColor.toColorInt())
        binding.imageBackground.setBackgroundColor(colour)

        binding.title.text = item.item.title
        val shortDesc = item.item.description?.split("<br>")?.firstOrNull()
        binding.description.show(!shortDesc.isNullOrEmpty())
        binding.description.text = shortDesc?.fromHtml()
        binding.source.text = item.item.url
        binding.date.text = item.item.date?.format(DateTimeFormatter.ofPattern("HH:mm 'at' dd MMM")) ?: ""
        binding.title.text = item.item.title
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