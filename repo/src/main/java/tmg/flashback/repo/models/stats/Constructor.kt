package tmg.flashback.repo.models.stats

/**
 * Data class for a specific constructor
 */
data class Constructor(
    val id: String,
    val name: String,
    val wikiUrl: String,
    val nationality: String,
    val nationalityISO: String,
    val color: Int
)

data class SlimConstructor(
    val id: String,
    val name: String,
    val color: Int
)