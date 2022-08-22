package tmg.flashback.statistics.network.models.constructors

fun Constructor.Companion.model(
    id: String = "constructorId",
    colour: String = "#123456",
    name: String = "name",
    photoUrl: String? = "photoUrl",
    nationality: String = "nationality",
    nationalityISO: String = "nationalityISO",
    wikiUrl: String? = "wikiUrl"
): Constructor = Constructor(
    id = id,
    colour = colour,
    name = name,
    photoUrl = photoUrl,
    nationality = nationality,
    nationalityISO = nationalityISO,
    wikiUrl = wikiUrl
)