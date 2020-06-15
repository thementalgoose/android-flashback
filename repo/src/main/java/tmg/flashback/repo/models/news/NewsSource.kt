package tmg.flashback.repo.models.news

data class NewsSource(
    val source: String,
    val image: String?,
    val link: String,
    val colour: String,
    val shortLink: String
)