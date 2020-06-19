package tmg.flashback.news.apis.motorsport

import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.news.apis.crashnet.CrashNetRssChannelModel
import tmg.flashback.news.apis.crashnet.crashNetDateFormat
import tmg.flashback.repo.models.news.Article
import tmg.flashback.repo.models.news.ArticleSource

fun MotorsportRssChannelModel.convert(): List<Article> {

    val source = ArticleSource(
        source = "Motorsport.com",
        link = "motorsport.com",
        colour = "#FFD806",
        textColor = "#181818",
        sourceShort = "MS"
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
                    motorsportDateFormat
                )),
                source = source
            )
        } ?: emptyList()
}