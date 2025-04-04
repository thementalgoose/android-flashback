package tmg.flashback.providers.model

import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.DriverEntry

fun DriverEntry.Companion.model(
    driver: Driver = Driver.model(),
    constructor: Constructor = Constructor.model()
): DriverEntry = DriverEntry(
    driver = driver,
    constructor = constructor
)