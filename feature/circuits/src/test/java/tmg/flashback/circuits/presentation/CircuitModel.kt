package tmg.flashback.circuits.presentation

import tmg.flashback.formula1.model.CircuitHistoryRace
import tmg.flashback.formula1.model.Location
import tmg.flashback.formula1.model.model

fun CircuitModel.Stats.Companion.model(
    circuitId: String = "circuitId",
    name: String = "circuitName",
    country: String = "country",
    countryISO: String = "countryISO",
    numberOfGrandPrix: Int = 0,
    startYear: Int? = null,
    endYear: Int? = null,
    wikipedia: String? = "wikiUrl",
    location: Location? = Location.model()
): CircuitModel.Stats = CircuitModel.Stats(
    circuitId = circuitId,
    name = name,
    country = country,
    countryISO = countryISO,
    numberOfGrandPrix = numberOfGrandPrix,
    startYear = startYear,
    endYear = endYear,
    wikipedia = wikipedia,
    location = location,
)

fun CircuitModel.Item.Companion.model(
    circuitId: String = "circuitId",
    circuitName: String = "circuitName",
    country: String = "country",
    countryISO: String = "countryISO",
    data: CircuitHistoryRace = CircuitHistoryRace.model()
): CircuitModel.Item = CircuitModel.Item(
    circuitId = circuitId,
    circuitName = circuitName,
    country = country,
    countryISO = countryISO,
    data = data
)