package tmg.flashback.rss.apis

import android.util.Log
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.repo.enums.SupportedArticleSource.Companion.getByLink
import tmg.flashback.repo.models.rss.Article
import tmg.flashback.repo.models.rss.ArticleSource
import tmg.flashback.repo.utils.md5
import tmg.flashback.rss.apis.model.RssXMLModel
import tmg.flashback.rss.apis.model.RssXMLModelItem
import java.net.URL

private const val dateFormat = "EEE, dd MMM yyyy HH:mm:ss Z"

fun RssXMLModel.convert(fromSource: String, showDescription: Boolean): List<Article> {

    if (this.channel == null) {
        Log.e("Flashback", "Failed to parse RSS model from channel $fromSource")
        return emptyList()
    }

    if (this.channel.title == null) {
        Log.e("Flashback", "Failed to parse RSS model from title $fromSource")
        return emptyList()
    }

    var url = this.channel.link ?: ""
    if (this.channel.link == null) {
        url = extractLinkSource(this.channel?.item) ?: ""
        if (url.isEmpty()) {
            Log.e("Flashback", "Failed to parse RSS model from link $fromSource")
            return emptyList()
        }
    }

    val source = getByLink(url)?.article ?: ArticleSource(
        title = channel!!.title!!,
        colour = "#4A34B6",
        textColor = "#FFFFFF",
        source = url,
        shortSource = null,
        rssLink = fromSource
    )

    return this.channel
        ?.item
        ?.filter { it.title != null && it.link != null && it.pubDate != null }
        ?.map {
            Article(
                id = it.link!!.md5(),
                title = it.title,
                description = when (showDescription) {
                    false -> null
                    true -> it.description
                },
                link = it.link!!,
                date = LocalDateTime.parse(it.pubDate!!, DateTimeFormatter.ofPattern(dateFormat)),
                source = source
            )
        } ?: emptyList()
}

private fun extractLinkSource(items: List<RssXMLModelItem>?): String? {
    val urlString = (items ?: emptyList())
        .filter { it.link != null }
        .map { it.link!! }
        .firstOrNull() ?: return null
    val url = URL(urlString)
    if (url.protocol != "http" && url.protocol != "https") {
        return null
    }
    return "${url.protocol}://${url.host}"
}
