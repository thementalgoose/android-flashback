package tmg.flashback.domain.persistence.models.constructors

import tmg.flashback.domain.persistence.models.drivers.Driver
import tmg.flashback.domain.persistence.models.drivers.model

fun ConstructorSeasonDriverWithDriver.Companion.model(
    results: ConstructorSeasonDriver = ConstructorSeasonDriver.model(),
    driver: Driver = Driver.model()
): ConstructorSeasonDriverWithDriver = ConstructorSeasonDriverWithDriver(
    results = results,
    driver = driver
)