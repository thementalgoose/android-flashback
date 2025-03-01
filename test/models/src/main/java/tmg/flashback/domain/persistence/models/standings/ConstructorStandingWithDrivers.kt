package tmg.flashback.data.persistence.models.standings

import tmg.flashback.data.persistence.models.constructors.Constructor
import tmg.flashback.data.persistence.models.constructors.model

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