package tmg.flashback.statistics.network.models.circuits

import tmg.flashback.statistics.network.models.constructors.Constructor
import tmg.flashback.statistics.network.models.constructors.model
import tmg.flashback.statistics.network.models.drivers.Driver
import tmg.flashback.statistics.network.models.drivers.model

fun CircuitPreviewPosition.Companion.model(
    position: Int = 1,
    driver: Driver = Driver.model(),
    constructor: Constructor = Constructor.model()
): CircuitPreviewPosition = CircuitPreviewPosition(
    position = position,
    driver = driver,
    constructor = constructor
)