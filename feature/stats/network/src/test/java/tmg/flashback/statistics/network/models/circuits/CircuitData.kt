package tmg.flashback.statistics.network.models.circuits

fun CircuitData.Companion.model(
    id: String = "",
    name: String = "",
    wikiUrl: String? = "",
    location: Location? = Location.model(),
    city: String = "city",
    country: String = "country",
    countryISO: String = "countryISO"
): CircuitData = CircuitData(
    id = id,
    name = name,
    wikiUrl = wikiUrl,
    location = location,
    city = city,
    country = country,
    countryISO = countryISO,
)