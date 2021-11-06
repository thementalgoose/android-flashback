package tmg.flashback.formula1.model

fun Constructor.Companion.model(
    id: String = "constructorId",
    name: String = "name",
    wikiUrl: String? = "wikiUrl",
    nationality: String = "nationality",
    nationalityISO: String = "nationalityISO",
    color: Int = 0,
): Constructor = Constructor(
    id = id,
    name = name,
    wikiUrl = wikiUrl,
    nationality = nationality,
    nationalityISO = nationalityISO,
    color = color
)