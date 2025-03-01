package tmg.flashback.providers.previewdata

import tmg.flashback.formula1.model.CircuitHistoryRaceResult
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.Driver

internal fun CircuitHistoryRaceResult.Companion.model(
    position: Int = 1,
    driver: Driver = Driver.model(),
    constructor: Constructor = Constructor.model()
): CircuitHistoryRaceResult = CircuitHistoryRaceResult(
    position = position,
    driver = driver,
    constructor = constructor
)