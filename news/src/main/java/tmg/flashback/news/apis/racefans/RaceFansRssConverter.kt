package tmg.flashback.news.apis.racefans

import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.news.apis.motorsport.MotorsportRssChannelModel
import tmg.flashback.news.apis.motorsport.motorsportDateFormat
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.repo.models.news.Article
import tmg.flashback.repo.models.news.ArticleSource
import tmg.flashback.repo.utils.md5

fun RaceFansRssChannelModel.convert(prefs: PrefsDB): List<Article> {

    val source = ArticleSource(
        source = "RaceFans.com",
        link = "racefans.com",
        colour = "#0077BA",
        textColor = "#f8f8f8",
        sourceShort = "RF"
    )

    return this.mItem
        ?.filter { it.mTitle != null && it.mGuid != null && it.mLink != null && it.mPubDate != null }
        ?.map {
            Article(
                id = it.mLink!!.md5(),
                title = it.mTitle!!,
                description = it.mDescription ?: "",
                link = it.mLink,
                date = LocalDateTime.parse(it.mPubDate!!, DateTimeFormatter.ofPattern(
                    motorsportDateFormat
                )),
                source = source,
                showDescription = prefs.newsShowDescription
            )
        } ?: emptyList()
}