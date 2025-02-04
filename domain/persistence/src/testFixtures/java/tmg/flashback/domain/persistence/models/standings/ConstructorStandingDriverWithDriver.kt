package tmg.flashback.domain.persistence.models.standings

import tmg.flashback.domain.persistence.models.drivers.Driver
import tmg.flashback.domain.persistence.models.drivers.model

fun ConstructorStandingDriverWithDriver.Companion.model(
    standing: ConstructorStandingDriver = ConstructorStandingDriver.model(),
    driver: Driver = Driver.model()
): ConstructorStandingDriverWithDriver = ConstructorStandingDriverWithDriver(
    standing = standing,
    driver = driver
)