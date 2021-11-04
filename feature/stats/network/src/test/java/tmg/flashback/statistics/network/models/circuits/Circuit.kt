package tmg.flashback.statistics.network.models.circuits

fun Circuit.Companion.model(
    id: String = "",
    name: String = "",
    wikiUrl: String? = "",
    location: Location? = Location.model(),
    city: String = "city",
    country: String = "country",
    countryISO: String = "countryISO"
): Circuit = Circuit(
    id = id,
    name = name,
    wikiUrl = wikiUrl,
    location = location,
    city = city,
    country = country,
    countryISO = countryISO,
)