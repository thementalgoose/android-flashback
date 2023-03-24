package tmg.flashback.stats.ui.circuits

import tmg.flashback.formula1.model.CircuitHistoryRace
import tmg.flashback.formula1.model.Location
import tmg.flashback.formula1.model.model

fun tmg.flashback.circuits.ui.CircuitModel.Stats.Companion.model(
    circuitId: String = "circuitId",
    name: String = "circuitName",
    country: String = "country",
    countryISO: String = "countryISO",
    numberOfGrandPrix: Int = 0,
    startYear: Int? = null,
    endYear: Int? = null,
    wikipedia: String? = "wikiUrl",
    location: Location? = Location.model()
): tmg.flashback.circuits.ui.CircuitModel.Stats = tmg.flashback.circuits.ui.CircuitModel.Stats(
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

fun tmg.flashback.circuits.ui.CircuitModel.Item.Companion.model(
    circuitId: String = "circuitId",
    circuitName: String = "circuitName",
    country: String = "country",
    countryISO: String = "countryISO",
    data: CircuitHistoryRace = CircuitHistoryRace.model()
): tmg.flashback.circuits.ui.CircuitModel.Item = tmg.flashback.circuits.ui.CircuitModel.Item(
    circuitId = circuitId,
    circuitName = circuitName,
    country = country,
    countryISO = countryISO,
    data = data
)