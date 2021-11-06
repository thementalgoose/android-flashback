package tmg.flashback.statistics.network.models.constructors

import kotlinx.serialization.Serializable

typealias AllConstructors = Map<String, Constructor>

@Serializable
data class Constructor(
    val id: String,
    val colour: String,
    val name: String,
    val nationality: String,
    val nationalityISO: String,
    val wikiUrl: String?
) {
    companion object
}