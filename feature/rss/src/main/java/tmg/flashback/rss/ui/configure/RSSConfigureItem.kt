package tmg.flashback.rss.ui.configure

import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import tmg.flashback.rss.R
import tmg.flashback.rss.repo.model.SupportedArticleSource
import java.net.MalformedURLException
import java.net.URL

sealed class RSSConfigureItem(
    val id: String
) {
    data class Header(
            @StringRes
            val text: Int,
            @StringRes
            val subtitle: Int? = null
    ) : RSSConfigureItem(
        id = "header-$text"
    )

    object NoItems : RSSConfigureItem(
        id = "no-items"
    )

    data class Item(
            val url: String,
            val urlModel: URL? = try { URL(url) } catch (e: MalformedURLException) { null },
            val supportedArticleSource: SupportedArticleSource?
    ) : RSSConfigureItem(
        id = "link-${url}"
    )

    data class QuickAdd(
            val supportedArticleSource: SupportedArticleSource,
            val urlModel: URL? = try { URL(supportedArticleSource.source) } catch (e: MalformedURLException) { null },
    ) : RSSConfigureItem(
        id = "link-${supportedArticleSource.rssLink}"
    )

    object Add : RSSConfigureItem(
        id = "add"
    )
}