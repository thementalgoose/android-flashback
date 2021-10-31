package tmg.flashback.statistics.network.models.constructors

fun ConstructorData.Companion.model(
    id: String = "constructorId",
    colour: String = "colour",
    name: String = "name",
    nationality: String = "nationality",
    nationalityISO: String = "nationalityISO",
    wikiUrl: String? = "wikiUrl"
): ConstructorData = ConstructorData(
    id = id,
    colour = colour,
    name = name,
    nationality = nationality,
    nationalityISO = nationalityISO,
    wikiUrl = wikiUrl,
)