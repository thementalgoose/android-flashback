package tmg.flashback.domain.persistence.models.standings

import tmg.flashback.domain.persistence.models.constructors.Constructor
import tmg.flashback.domain.persistence.models.constructors.model

fun DriverStandingConstructorWithConstructor.Companion.model(
    standing: DriverStandingConstructor = DriverStandingConstructor.model(),
    constructor: Constructor = Constructor.model()
): DriverStandingConstructorWithConstructor = DriverStandingConstructorWithConstructor(
    standing = standing,
    constructor = constructor
)