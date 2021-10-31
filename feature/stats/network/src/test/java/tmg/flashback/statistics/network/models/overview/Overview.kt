package tmg.flashback.statistics.network.models.overview

fun OverviewRace.Companion.model(
    season: Int = 2020,
    round: Int = 1,
    name: String = "name",
    circuit: String = "circuit",
    circuitId: String = "circuitId",
    country: String = "country",
    countryISO: String = "countryISO",
    date: String = "1995-10-12",
    time: String? = "12:34",
    hasQualifying: Boolean = true,
    hasRace: Boolean = false
): OverviewRace = OverviewRace(
    season = season,
    round = round,
    name = name,
    circuit = circuit,
    circuitId = circuitId,
    country = country,
    countryISO = countryISO,
    date = date,
    time = time,
    hasQualifying = hasQualifying,
    hasRace = hasRace
)