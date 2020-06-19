package tmg.flashback.repo.models.news

import org.threeten.bp.LocalDateTime

data class Article(
    val id: String,
    val title: String,
    val description: String,
    val link: String,
    val date: LocalDateTime?,
    val source: ArticleSource
)