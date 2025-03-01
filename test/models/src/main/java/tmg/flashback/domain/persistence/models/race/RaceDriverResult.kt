package tmg.flashback.data.persistence.models.race

import tmg.flashback.data.persistence.models.constructors.Constructor
import tmg.flashback.data.persistence.models.constructors.model
import tmg.flashback.data.persistence.models.drivers.Driver
import tmg.flashback.data.persistence.models.drivers.model

fun RaceDriverResult.Companion.model(
    raceResult: RaceResult = RaceResult.model(),
    driver: Driver = Driver.model(),
    constructor: Constructor = Constructor.model()
): RaceDriverResult = RaceDriverResult(
    raceResult = raceResult,
    driver = driver,
    constructor = constructor
)