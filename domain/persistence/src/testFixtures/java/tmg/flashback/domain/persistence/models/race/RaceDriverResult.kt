package tmg.flashback.domain.persistence.models.race

import tmg.flashback.domain.persistence.models.constructors.Constructor
import tmg.flashback.domain.persistence.models.constructors.model
import tmg.flashback.domain.persistence.models.drivers.Driver
import tmg.flashback.domain.persistence.models.drivers.model

fun RaceDriverResult.Companion.model(
    raceResult: RaceResult = RaceResult.model(),
    driver: Driver = Driver.model(),
    constructor: Constructor = Constructor.model()
): RaceDriverResult = RaceDriverResult(
    raceResult = raceResult,
    driver = driver,
    constructor = constructor
)