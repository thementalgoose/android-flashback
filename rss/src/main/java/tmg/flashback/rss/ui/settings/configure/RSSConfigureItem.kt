package tmg.flashback.rss.ui.settings.configure

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import tmg.flashback.rss.repo.model.SupportedArticleSource

sealed class RSSConfigureItem {
    data class Header(
            @StringRes
            val text: Int,
            @StringRes
            val subtitle: Int
    ) : RSSConfigureItem()

    object NoItems : RSSConfigureItem()

    data class Item(
            val url: String,
            val supportedArticleSource: SupportedArticleSource?
    ) : RSSConfigureItem()

    data class QuickAdd(
            val supportedArticleSource: SupportedArticleSource
    ) : RSSConfigureItem() {
        val sourceColor: Color by lazy { Color(supportedArticleSource.colour) }
    }

    object Add : RSSConfigureItem()
}