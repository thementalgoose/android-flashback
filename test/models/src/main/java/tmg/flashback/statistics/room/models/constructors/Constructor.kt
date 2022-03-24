package tmg.flashback.statistics.room.models.constructors

fun Constructor.Companion.model(
    id: String = "constructorId",
    colour: String = "#123456",
    name: String = "name",
    nationality: String = "nationality",
    nationalityISO: String = "nationalityISO",
    wikiUrl: String? = "wikiUrl"
): Constructor = Constructor(
    id = id,
    colour = colour,
    name = name,
    nationality = nationality,
    nationalityISO = nationalityISO,
    wikiUrl = wikiUrl,
)