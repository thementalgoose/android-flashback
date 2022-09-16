package tmg.flashback.rss.ui.configure

import tmg.flashback.rss.repo.model.SupportedArticleSource
import java.net.MalformedURLException
import java.net.URL

data class RSSSource(
    val url: String,
    val supportedArticleSource: SupportedArticleSource?,
    val isChecked: Boolean
) {
    val urlModel: URL? = try { URL(url) } catch (e: MalformedURLException) { null }
}