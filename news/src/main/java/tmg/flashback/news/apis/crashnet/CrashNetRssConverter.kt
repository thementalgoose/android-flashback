package tmg.flashback.news.apis.crashnet

import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.repo.models.news.Article
import tmg.flashback.repo.models.news.ArticleSource
import tmg.flashback.repo.utils.md5

fun CrashNetRssChannelModel.convert(prefs: PrefsDB): List<Article> {

    val source = ArticleSource(
        source = "Crash.net ",
        link = "crash.com",
        colour = "#E91B1C",
        sourceShort = "C"
    )

    return this.mItem
        ?.filter { it.mTitle != null && it.mGuid != null && it.mLink != null && it.mPubDate != null }
        ?.map {
            Article(
                id = it.mLink!!.md5(),
                title = it.mTitle!!.trim().replace("&#039;", "'"),
                description = it.mDescription ?: "",
                link = it.mLink.replace("http://", "https://"),
                date = LocalDateTime.parse(it.mPubDate!!, DateTimeFormatter.ofPattern(
                    crashNetDateFormat
                )),
                source = source,
                showDescription = prefs.newsShowDescription
            )
        } ?: emptyList()
}