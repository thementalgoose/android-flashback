package tmg.flashback.statistics.room.models.standings

import tmg.flashback.statistics.room.models.drivers.Driver
import tmg.flashback.statistics.room.models.drivers.model

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