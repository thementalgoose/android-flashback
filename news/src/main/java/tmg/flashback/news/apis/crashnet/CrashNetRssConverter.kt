package tmg.flashback.news.apis.crashnet

import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.repo.models.news.Article
import tmg.flashback.repo.models.news.ArticleSource

fun CrashNetRssChannelModel.convert(): List<Article> {

    val source = ArticleSource(
        source = "Crash.net ",
        link = "crash.com",
        colour = "#afb500",
        sourceShort = "C"
    )

    return this.mItem
        ?.filter { it.mTitle != null && it.mGuid != null && it.mLink != null && it.mPubDate != null }
        ?.map {
            Article(
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