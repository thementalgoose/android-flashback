package tmg.flashback.statistics.network.models.constructors

data class ConstructorData(
    val id: String,
    val colour: String,
    val name: String,
    val nationality: String,
    val nationalityISO: String,
    val wikiUrl: String?
)