package tmg.flashback.providers.model

import tmg.flashback.formula1.model.DriverEntry
import tmg.flashback.formula1.model.LapTime
import tmg.flashback.formula1.model.SprintQualifyingResult

fun SprintQualifyingResult.Companion.model(
    driver: DriverEntry = DriverEntry.model(),
    lapTime: LapTime? = LapTime.model(),
    position: Int = 1
): SprintQualifyingResult = SprintQualifyingResult(
    entry = driver,
    lapTime = lapTime,
    position = position
)