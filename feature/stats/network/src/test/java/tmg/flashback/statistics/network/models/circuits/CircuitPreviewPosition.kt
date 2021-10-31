package tmg.flashback.statistics.network.models.circuits

import tmg.flashback.statistics.network.models.constructors.ConstructorData
import tmg.flashback.statistics.network.models.constructors.model
import tmg.flashback.statistics.network.models.drivers.DriverData
import tmg.flashback.statistics.network.models.drivers.model

fun CircuitPreviewPosition.Companion.model(
    position: Int = 1,
    driver: DriverData = DriverData.model(),
    constructor: ConstructorData = ConstructorData.model()
): CircuitPreviewPosition = CircuitPreviewPosition(
    position = position,
    driver = driver,
    constructor = constructor
)