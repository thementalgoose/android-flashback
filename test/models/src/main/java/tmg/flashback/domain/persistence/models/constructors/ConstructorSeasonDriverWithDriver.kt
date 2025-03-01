package tmg.flashback.data.persistence.models.constructors

import tmg.flashback.data.persistence.models.drivers.Driver
import tmg.flashback.data.persistence.models.drivers.model

fun ConstructorSeasonDriverWithDriver.Companion.model(
    results: ConstructorSeasonDriver = ConstructorSeasonDriver.model(),
    driver: Driver = Driver.model()
): ConstructorSeasonDriverWithDriver = ConstructorSeasonDriverWithDriver(
    results = results,
    driver = driver
)