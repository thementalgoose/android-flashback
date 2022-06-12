package tmg.flashback.stats.ui.circuits

import org.junit.jupiter.api.Assertions.*
import tmg.flashback.formula1.model.CircuitHistory
import tmg.flashback.formula1.model.CircuitHistoryRace
import tmg.flashback.formula1.model.Location
import tmg.flashback.formula1.model.model

fun CircuitModel.Stats.Companion.model(
    circuitId: String = "circuitId",
    name: String = "name",
    country: String = "country",
    countryISO: String = "countryISO",
    numberOfGrandPrix: Int = 1,
    startYear: Int? = 2020,
    endYear: Int? = 2020,
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
    data: CircuitHistoryRace = CircuitHistoryRace.model()
): CircuitModel.Item = CircuitModel.Item(
    data = data
)