package tmg.flashback.rss.repo.model

import org.threeten.bp.LocalDateTime

data class Article(
        val id: String,
        val title: String,
        val description: String?,
        val link: String,
        val date: LocalDateTime?,
        val source: ArticleSource
)