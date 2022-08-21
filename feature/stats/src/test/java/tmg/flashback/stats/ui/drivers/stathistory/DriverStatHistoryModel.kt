package tmg.flashback.stats.ui.drivers.stathistory

import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.RaceInfo
import tmg.flashback.formula1.model.model

fun DriverStatHistoryModel.Companion.modelYear(
    season: Int = 2020
): DriverStatHistoryModel.Year = DriverStatHistoryModel.Year(
    season = season
)

fun DriverStatHistoryModel.Companion.modelRace(
    raceInfo: RaceInfo = RaceInfo.model(),
    constructor: Constructor? = Constructor.model(),
): DriverStatHistoryModel.Race = DriverStatHistoryModel.Race(
    raceInfo = raceInfo,
    constructor = constructor
)


fun DriverStatHistoryModel.Companion.modelRacePosition(
    position: Int = 1,
    raceInfo: RaceInfo = RaceInfo.model(),
    constructor: Constructor? = Constructor.model(),
): DriverStatHistoryModel.RacePosition = DriverStatHistoryModel.RacePosition(
    position = position,
    raceInfo = raceInfo,
    constructor = constructor
)

fun DriverStatHistoryModel.Companion.modelLabel(
    text: String = "label"
): DriverStatHistoryModel.Label = DriverStatHistoryModel.Label(
    text = text
)