package tmg.flashback.news.apis.autosport

import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.repo.models.news.NewsItem
import tmg.flashback.repo.models.news.NewsSource

fun AutosportRssChannelModel.convert(): List<NewsItem> {

    val source = NewsSource(
        source = "Autosport",
        image = this.mImage?.mUrl,
        link = "autosport.com",
        colour = "#ff0000",
        shortLink = "AS"
    )

    return this.mItem
        ?.filter { it.mTitle != null && it.mGuid != null && it.mLink != null && it.mPubDate != null }
        ?.map {
            NewsItem(
                id = it.mGuid!!,
                title = it.mTitle!!,
                description = it.mDescription ?: "",
                link = it.mLink!!,
                date = LocalDateTime.parse(it.mPubDate!!, DateTimeFormatter.ofPattern(autosportDateFormat)),
                source = source
            )
        } ?: emptyList<NewsItem>()
}