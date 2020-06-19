package tmg.flashback.repo.models.news

data class ArticleSource(
    val source: String,
    val colour: String,
    val sourceShort: String,
    val textColor: String = "#ffffff",
    val link: String
)