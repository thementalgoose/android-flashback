package tmg.flashback.rss.network.apis

import android.util.Log
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import tmg.flashback.rss.network.model.RssXMLModel
import tmg.flashback.rss.network.model.RssXMLModelItem
import tmg.flashback.rss.repo.model.Article
import tmg.flashback.rss.repo.model.ArticleSource
import tmg.flashback.rss.usecases.GetSupportedSourceUseCase
import tmg.utilities.extensions.md5
import java.net.URL
import java.util.Locale

private const val dateFormat = "EEE, d MMM yyyy HH:mm:ss Z"

internal fun RssXMLModel.convert(getSupportedSourceUseCase: GetSupportedSourceUseCase, fromSource: String, showDescription: Boolean): List<Article> {

    if (this.channel == null) {
        Log.e("RSS", "Failed to parse RSS model from channel $fromSource")
        return emptyList()
    }

    if (this.channel.title == null) {
        Log.e("RSS", "Failed to parse RSS model from title $fromSource")
        return emptyList()
    }

    var url = this.channel.link ?: ""
    if (this.channel.link == null) {
        url = extractLinkSource(this.channel?.item) ?: ""
        if (url.isEmpty()) {
            Log.e("RSS", "Failed to parse RSS model from link $fromSource")
            return emptyList()
        }
    }

    val source = getSupportedSourceUseCase.getByLink(url)?.article ?: ArticleSource(
        title = channel!!.title!!,
        colour = "#4A34B6",
        textColor = "#FFFFFF",
        source = url,
        shortSource = null,
        rssLink = fromSource,
        contactLink = null
    )

    return this.channel
        ?.item
        ?.filter { it.title != null && it.link != null && it.pubDate != null }
        ?.map {
            Article(
                id = it.link!!.md5,
                title = it.title.replace("&#039;", "'"),
                description = when (showDescription) {
                    false -> null
                    true -> it.description
                        ?.substringBefore("&lt;/p&gt;")
                        ?.substringBefore("</p>")
                        ?.trim()
                },
                link = it.link!!.replace("http://", "https://"),
                date = LocalDateTime.parse(it.pubDate!!.replace("GMT", "+0000"), DateTimeFormatter.ofPattern(dateFormat, Locale.ENGLISH)),
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
