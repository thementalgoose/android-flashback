package tmg.flashback.statistics.room.models.circuit

import tmg.flashback.statistics.room.models.constructors.Constructor
import tmg.flashback.statistics.room.models.constructors.model
import tmg.flashback.statistics.room.models.drivers.Driver
import tmg.flashback.statistics.room.models.drivers.model

fun CircuitRoundResultWithDriverConstructor.Companion.model(
    result: CircuitRoundResult = CircuitRoundResult.model(),
    constructor: Constructor = Constructor.model(),
    driver: Driver = Driver.model()
): CircuitRoundResultWithDriverConstructor = CircuitRoundResultWithDriverConstructor(
    result = result,
    constructor = constructor,
    driver = driver
)