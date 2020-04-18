package tmg.flashback.repo.models

/**
 * Data class for a specific constructor
 */
open class Constructor(
    val id: String,
    val name: String,
    val wikiUrl: String,
    val nationality: String,
    val nationalityISO: String,
    val color: Int
)