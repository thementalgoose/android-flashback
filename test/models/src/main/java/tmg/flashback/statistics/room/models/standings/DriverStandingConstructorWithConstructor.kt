package tmg.flashback.statistics.room.models.standings

import tmg.flashback.statistics.room.models.constructors.Constructor
import tmg.flashback.statistics.room.models.constructors.model

fun DriverStandingConstructorWithConstructor.Companion.model(
    standing: DriverStandingConstructor = DriverStandingConstructor.model(),
    constructor: Constructor = Constructor.model()
): DriverStandingConstructorWithConstructor = DriverStandingConstructorWithConstructor(
    standing = standing,
    constructor = constructor
)