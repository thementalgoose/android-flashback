package tmg.flashback.rss.repo.model

data class ArticleSource(
        val title: String,
        val colour: String,
        val textColor: String = "#ffffff",
        val rssLink: String,
        val source: String,
        val shortSource: String?,
        val contactLink: String?
)