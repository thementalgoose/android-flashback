package tmg.flashback.formula1.model

/**
 * Data class for a specific constructor
 */
data class Constructor(
    val id: String,
    val name: String,
    val wikiUrl: String?,
    val nationality: String,
    val nationalityISO: String,
    val color: Int
) {
    @Deprecated("Should just use regular constructor model")
    fun toSlim() = SlimConstructor(
        id = id,
        name = name,
        color = color
    )
}

@Deprecated("Should just use regular constructor model")
data class SlimConstructor(
    val id: String,
    val name: String,
    val color: Int
)