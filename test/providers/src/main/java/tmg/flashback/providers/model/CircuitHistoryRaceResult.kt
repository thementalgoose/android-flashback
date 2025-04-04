package tmg.flashback.providers.model

import tmg.flashback.formula1.model.CircuitHistoryRaceResult
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.Driver

fun CircuitHistoryRaceResult.Companion.model(
    position: Int = 1,
    driver: Driver = Driver.model(),
    constructor: Constructor = Constructor.model()
): CircuitHistoryRaceResult = CircuitHistoryRaceResult(
    position = position,
    driver = driver,
    constructor = constructor
)