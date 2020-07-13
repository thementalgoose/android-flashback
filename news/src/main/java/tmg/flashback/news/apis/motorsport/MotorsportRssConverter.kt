package tmg.flashback.news.apis.motorsport

import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.repo.models.news.Article
import tmg.flashback.repo.models.news.ArticleSource
import tmg.flashback.repo.utils.md5

fun MotorsportRssChannelModel.convert(prefs: PrefsDB): List<Article> {

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
                id = it.mLink!!.md5(),
                title = it.mTitle!!,
                description = it.mDescription ?: "",
                link = it.mLink.replace("http://", "https://"),
                date = LocalDateTime.parse(it.mPubDate!!, DateTimeFormatter.ofPattern(
                    motorsportDateFormat
                )),
                source = source,
                showDescription = prefs.newsShowDescription
            )
        } ?: emptyList()
}