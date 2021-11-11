package tmg.flashback.statistics.room.models.standings

import tmg.flashback.statistics.room.models.drivers.Driver
import tmg.flashback.statistics.room.models.drivers.model

fun ConstructorStandingDriverWithDriver.Companion.model(
    standing: ConstructorStandingDriver = ConstructorStandingDriver.model(),
    driver: Driver = Driver.model()
): ConstructorStandingDriverWithDriver = ConstructorStandingDriverWithDriver(
    standing = standing,
    driver = driver
)