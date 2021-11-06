package tmg.flashback.statistics.room.models.standings

import tmg.flashback.statistics.room.models.constructors.Constructor
import tmg.flashback.statistics.room.models.constructors.model

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