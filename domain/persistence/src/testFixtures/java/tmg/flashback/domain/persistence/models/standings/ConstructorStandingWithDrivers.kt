package tmg.flashback.domain.persistence.models.standings

import tmg.flashback.domain.persistence.models.constructors.Constructor
import tmg.flashback.domain.persistence.models.constructors.model

fun ConstructorStandingWithDrivers.Companion.model(
    standing: ConstructorStanding = ConstructorStanding.model(),
    constructor: Constructor = Constructor.model(),
    drivers: List<ConstructorStandingDriverWithDriver> = listOf(
        ConstructorStandingDriverWithDriver.model()
    )
): ConstructorStandingWithDrivers = ConstructorStandingWithDrivers(
    standing = standing,
    constructor = constructor,
    drivers = drivers
)