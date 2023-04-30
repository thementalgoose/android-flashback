package tmg.flashback.domain.persistence.models.standings

import tmg.flashback.domain.persistence.models.drivers.Driver
import tmg.flashback.domain.persistence.models.drivers.model

fun DriverStandingWithConstructors.Companion.model(
    standing: DriverStanding = DriverStanding.model(),
    driver: Driver = Driver.model(),
    constructors: List<DriverStandingConstructorWithConstructor> = listOf(
        DriverStandingConstructorWithConstructor.model()
    )
): DriverStandingWithConstructors = DriverStandingWithConstructors(
    standing = standing,
    driver = driver,
    constructors = constructors
)