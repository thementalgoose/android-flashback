package tmg.flashback.rss.ui.settings.configure.viewholders

import android.annotation.SuppressLint
import android.view.View
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.rss.R
import tmg.flashback.rss.databinding.ViewRssConfigureItemBinding
import tmg.flashback.rss.repo.model.SupportedArticleSource
import tmg.flashback.rss.ui.settings.configure.RSSConfigureItem
import tmg.utilities.extensions.getColor
import tmg.utilities.extensions.views.context
import tmg.utilities.extensions.views.gone
import tmg.utilities.extensions.views.show
import java.net.MalformedURLException
import java.net.URL

internal class ItemViewHolder(
    private val removeItem: (String) -> Unit,
    private val visitWebsite: (SupportedArticleSource) -> Unit,
    private val binding: ViewRssConfigureItemBinding
): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    private lateinit var item: RSSConfigureItem.Item

    init {
        binding.remove.setOnClickListener(this)
        binding.visitWebsite.setOnClickListener(this)
    }

    fun bind(item: RSSConfigureItem.Item) {
        this.item = item
        binding.root.alpha = 1.0f
        if (item.supportedArticleSource != null) {
            binding.imageBackground.setBackgroundColor(item.supportedArticleSource.colour.toColorInt())
            binding.label.text = item.supportedArticleSource.sourceShort
            binding.label.setTextColor(item.supportedArticleSource.textColour.toColorInt())
            binding.title.text = item.supportedArticleSource.source
            binding.description.text = item.supportedArticleSource.rssLink
            binding.visitWebsite.show()
        }
        else {
            binding.imageBackground.setBackgroundColor(context.theme.getColor(R.attr.colorPrimary))
            binding.label.text = "..."
            binding.visitWebsite.gone()

            try {
                val url = URL(item.url)
                @SuppressLint("SetTextI18n")
                binding.title.text = "${url.protocol}://${url.host}"
                binding.description.text = item.url
            } catch (e: MalformedURLException) {
                binding.title.text = item.url
                binding.description.setText(R.string.rss_configure_malformed_url)
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.remove -> {
                removeItem(item.supportedArticleSource?.rssLink ?: item.url)
            }
            binding.visitWebsite -> {
                this.item.supportedArticleSource?.let {
                    visitWebsite(it)
                }
            }
        }
    }
}