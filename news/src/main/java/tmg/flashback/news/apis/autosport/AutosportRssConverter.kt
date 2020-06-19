package tmg.flashback.news.apis.autosport

import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.repo.models.news.Article
import tmg.flashback.repo.models.news.ArticleSource

fun AutosportRssChannelModel.convert(): List<Article> {

    val source = ArticleSource(
        source = "Autosport",
        colour = "#ff0000",
        link = "autosport.com",
        sourceShort = "AS"
    )

    return this.mItem
        ?.filter { it.mTitle != null && it.mGuid != null && it.mLink != null && it.mPubDate != null }
        ?.map {
            Article(
                id = it.mGuid!!,
                title = it.mTitle!!,
                description = it.mDescription ?: "",
                link = it.mLink!!,
                date = LocalDateTime.parse(it.mPubDate!!, DateTimeFormatter.ofPattern(autosportDateFormat)),
                source = source
            )
        } ?: emptyList()
}