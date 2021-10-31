package tmg.flashback.statistics.room.models.overview

fun Overview.Companion.model(
    season: Int = 2020,
    round: Int = 1,
    name: String = "name",
    circuitName: String = "circuit",
    circuitId: String = "circuitId",
    country: String = "country",
    countryISO: String = "countryISO",
    date: String = "1995-10-12",
    time: String? = "12:34",
    hasRace: Boolean = false,
    hasQualifying: Boolean = true,
    id: String = "${season}_${round}"
): Overview = Overview(
    season = season,
    round = round,
    name = name,
    circuitName = circuitName,
    circuitId = circuitId,
    country = country,
    countryISO = countryISO,
    date = date,
    time = time,
    hasRace = hasRace,
    hasQualifying = hasQualifying,
    id = id
)