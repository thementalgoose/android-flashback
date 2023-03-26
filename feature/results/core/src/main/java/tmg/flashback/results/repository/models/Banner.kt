package tmg.flashback.results.repository.models

data class Banner(
    val message: String,
    val url: String? = null,
    val highlight: Boolean,
    val season: Int?
)