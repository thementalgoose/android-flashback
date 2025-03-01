package tmg.flashback.providers.previewdata

import tmg.flashback.formula1.model.DriverEntry
import tmg.flashback.formula1.model.LapTime
import tmg.flashback.formula1.model.QualifyingResult

internal fun QualifyingResult.Companion.model(
    driver: DriverEntry = DriverEntry.model(),
    lapTime: LapTime? = LapTime.model(),
    position: Int = 1,
): QualifyingResult = QualifyingResult(
    entry = driver,
    lapTime = lapTime,
    position = position
)