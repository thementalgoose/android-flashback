package tmg.flashback.news.apis.skysports_unofficial

import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.news.apis.autosport.autosportDateFormat
import tmg.flashback.news.apis.pitpass.PitPassRssChannelModel
import tmg.flashback.repo.models.news.Article
import tmg.flashback.repo.models.news.ArticleSource
import tmg.flashback.repo.utils.md5
import java.util.*

fun List<SkySportsModel>.convert(): List<Article> {

    val source = ArticleSource(
        source = "SkySports (Unofficial)",
        colour = "#482D82",
        link = "skysports.com",
        sourceShort = "SS"
    )

    return this
        .filter { it.title != null && it.link != null }
        .map {
            Article(
                id = it.link!!.md5(),
                title = it.title!!.trim(),
                description = it.shortdesc ?: "",
                link = it.link.replace("http://", "https://"),
                date = null,
                source = source
            )
        }
}