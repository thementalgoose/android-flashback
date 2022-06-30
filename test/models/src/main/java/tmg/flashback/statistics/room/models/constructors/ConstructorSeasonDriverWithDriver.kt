package tmg.flashback.statistics.room.models.constructors

import tmg.flashback.statistics.room.models.drivers.Driver
import tmg.flashback.statistics.room.models.drivers.model

fun ConstructorSeasonDriverWithDriver.Companion.model(
    results: ConstructorSeasonDriver = ConstructorSeasonDriver.model(),
    driver: Driver = Driver.model()
): ConstructorSeasonDriverWithDriver = ConstructorSeasonDriverWithDriver(
    results = results,
    driver = driver
)