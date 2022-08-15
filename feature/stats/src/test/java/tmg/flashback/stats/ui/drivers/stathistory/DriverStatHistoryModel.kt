package tmg.flashback.stats.ui.drivers.stathistory

import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.model

fun DriverStatHistoryModel.Companion.modelYear(
    season: Int = 2020
): DriverStatHistoryModel.Year = DriverStatHistoryModel.Year(
    season = season
)

fun DriverStatHistoryModel.Companion.modelRace(
    raceName: String = "name",
    nationality: String = "country",
    nationalityISO: String = "countryISO",
    season: Int = 2020,
    round: Int = 1,
    circuitName: String = "circuitName",
    circuitId: String = "circuitId",
    constructor: Constructor? = Constructor.model(),
): DriverStatHistoryModel.Race = DriverStatHistoryModel.Race(
    raceName = raceName,
    nationality = nationality,
    nationalityISO = nationalityISO,
    season = season,
    round = round,
    circuitName = circuitName,
    circuitId = circuitId,
    constructor = constructor
)

fun DriverStatHistoryModel.Companion.modelLabel(
    text: String = "label"
): DriverStatHistoryModel.Label = DriverStatHistoryModel.Label(
    text = text
)