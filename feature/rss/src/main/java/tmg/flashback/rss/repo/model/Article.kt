package tmg.flashback.rss.repo.model

import java.time.LocalDateTime

data class Article(
        val id: String,
        val title: String,
        val description: String?,
        val link: String,
        val date: LocalDateTime?,
        val source: ArticleSource
)