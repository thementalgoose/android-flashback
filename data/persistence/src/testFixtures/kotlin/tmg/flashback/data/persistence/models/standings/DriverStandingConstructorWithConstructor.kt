package tmg.flashback.data.persistence.models.standings

import tmg.flashback.data.persistence.models.constructors.Constructor
import tmg.flashback.data.persistence.models.constructors.model

fun DriverStandingConstructorWithConstructor.Companion.model(
    standing: DriverStandingConstructor = DriverStandingConstructor.model(),
    constructor: Constructor = Constructor.model()
): DriverStandingConstructorWithConstructor = DriverStandingConstructorWithConstructor(
    standing = standing,
    constructor = constructor
)