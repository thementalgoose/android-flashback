package tmg.flashback.providers.previewdata

import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.DriverEntry

internal fun DriverEntry.Companion.model(
    driver: Driver = Driver.model(),
    constructor: Constructor = Constructor.model()
): DriverEntry = DriverEntry(
    driver = driver,
    constructor = constructor
)