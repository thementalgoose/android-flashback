package tmg.flashback.domain.persistence.models.circuit

import tmg.flashback.domain.persistence.models.constructors.Constructor
import tmg.flashback.domain.persistence.models.constructors.model
import tmg.flashback.domain.persistence.models.drivers.Driver
import tmg.flashback.domain.persistence.models.drivers.model

fun CircuitRoundResultWithDriverConstructor.Companion.model(
    result: CircuitRoundResult = CircuitRoundResult.model(),
    constructor: Constructor = Constructor.model(),
    driver: Driver = Driver.model()
): CircuitRoundResultWithDriverConstructor = CircuitRoundResultWithDriverConstructor(
    result = result,
    constructor = constructor,
    driver = driver
)