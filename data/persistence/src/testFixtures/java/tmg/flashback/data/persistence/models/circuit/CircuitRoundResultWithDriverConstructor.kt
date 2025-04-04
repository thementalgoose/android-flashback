package tmg.flashback.data.persistence.models.circuit

import tmg.flashback.data.persistence.models.constructors.Constructor
import tmg.flashback.data.persistence.models.constructors.model
import tmg.flashback.data.persistence.models.drivers.Driver
import tmg.flashback.data.persistence.models.drivers.model

fun CircuitRoundResultWithDriverConstructor.Companion.model(
    result: CircuitRoundResult = CircuitRoundResult.model(),
    constructor: Constructor = Constructor.model(),
    driver: Driver = Driver.model()
): CircuitRoundResultWithDriverConstructor = CircuitRoundResultWithDriverConstructor(
    result = result,
    constructor = constructor,
    driver = driver
)