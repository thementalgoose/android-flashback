package tmg.flashback.news.apis.pitpass

import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.news.apis.autosport.AutosportRssChannelModel
import tmg.flashback.news.apis.autosport.autosportDateFormat
import tmg.flashback.repo.models.news.NewsItem
import tmg.flashback.repo.models.news.NewsSource

fun PitPassRssChannelModel.convert(): List<NewsItem> {

    val source = NewsSource(
        source = "PitPass",
        colour = "#611818",
        link = "pitpass.com",
        sourceShort = "PP"
    )

    return this.mItem
        ?.filter { it.mTitle != null && it.mGuid != null && it.mLink != null && it.mPubDate != null }
        ?.map {
            NewsItem(
                id = it.mGuid!!,
                title = it.mTitle!!,
                description = it.mDescription ?: "",
                link = it.mLink!!,
                date = LocalDateTime.parse(it.mPubDate!!, DateTimeFormatter.ofPattern(
                    autosportDateFormat
                )),
                source = source
            )
        } ?: emptyList()
}