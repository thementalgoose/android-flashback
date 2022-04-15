package tmg.flashback.statistics.ui.search

import org.threeten.bp.LocalDate

internal fun SearchItem.Companion.constructorModel(
    constructorId: String = "constructorId",
    name: String = "name",
    nationality: String = "nationality",
    nationalityISO: String = "nationalityISO",
    colour: Int = 0,
): SearchItem.Constructor = SearchItem.Constructor(
    constructorId = constructorId,
    name = name,
    nationality = nationality,
    nationalityISO = nationalityISO,
    colour = colour
)

internal fun SearchItem.Companion.driverModel(
    driverId: String = "driverId",
    name: String = "firstName lastName",
    nationality: String = "nationality",
    nationalityISO: String = "nationalityISO",
    imageUrl: String? = "photoUrl"
): SearchItem.Driver = SearchItem.Driver(
    driverId = driverId,
    name = name,
    nationality = nationality,
    nationalityISO = nationalityISO,
    imageUrl = imageUrl,
)

internal fun SearchItem.Companion.raceModel(
    season: Int = 2020,
    round: Int = 1,
    raceId: String = "${season}-${round}",
    raceName: String = "name",
    country: String = "country",
    countryISO: String = "countryISO",
    circuitId: String = "circuitId",
    circuitName: String = "circuitName",
    date: LocalDate = LocalDate.of(2020, 10, 12)
): SearchItem.Race = SearchItem.Race(
    raceId = raceId,
    season = season,
    round = round,
    raceName = raceName,
    country = country,
    countryISO = countryISO,
    circuitId = circuitId,
    circuitName = circuitName,
    date = date,
)

internal fun SearchItem.Companion.circuitModel(
    circuitId: String = "circuitId",
    name: String = "circuitName",
    nationality: String = "country",
    nationalityISO: String = "countryISO",
    location: String = "city"
): SearchItem.Circuit = SearchItem.Circuit(
    circuitId = circuitId,
    name = name,
    nationality = nationality,
    nationalityISO = nationalityISO,
    location = location
)