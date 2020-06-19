package tmg.flashback.news.apis.crashnet

import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.news.apis.autosport.autosportDateFormat
import tmg.flashback.repo.models.news.NewsItem
import tmg.flashback.repo.models.news.NewsSource

fun CrashNetRssChannelModel.convert(): List<NewsItem> {

    val source = NewsSource(
        source = "Crash.net ",
        link = "crash.com",
        colour = "#afb500",
        sourceShort = "C"
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
                    crashNetDateFormat
                )),
                source = source
            )
        } ?: emptyList()
}