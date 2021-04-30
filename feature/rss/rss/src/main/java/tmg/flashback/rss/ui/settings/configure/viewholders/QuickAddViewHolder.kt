package tmg.flashback.rss.ui.settings.configure.viewholders

import android.view.View
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.rss.databinding.ViewRssConfigureQuickaddBinding
import tmg.flashback.rss.repo.model.SupportedArticleSource
import tmg.flashback.rss.ui.settings.configure.RSSConfigureItem

class QuickAddViewHolder(
    private val quickAddItem: (SupportedArticleSource) -> Unit,
    private val websiteLink: (SupportedArticleSource) -> Unit,
    private val binding: ViewRssConfigureQuickaddBinding
): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    private lateinit var item: SupportedArticleSource

    init {
        binding.add.setOnClickListener(this)
        binding.visitWebsite.setOnClickListener(this)
    }

    fun bind(item: RSSConfigureItem.QuickAdd) {

        binding.root.alpha = 0.8f

        this.item = item.supportedArticleSource
        binding.imageBackground.setBackgroundColor(item.supportedArticleSource.colour.toColorInt())
        binding.label.text = item.supportedArticleSource.sourceShort
        binding.label.setTextColor(item.supportedArticleSource.textColour.toColorInt())
        binding.title.text = item.supportedArticleSource.source
        binding.description.text = item.supportedArticleSource.rssLink
        binding.description.isSelected = true
    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.add -> {
                quickAddItem(item)
            }
            binding.visitWebsite -> {
                websiteLink(item)
            }
        }
    }
}