package tmg.flashback.data.persistence.models.race

import tmg.flashback.data.persistence.models.constructors.Constructor
import tmg.flashback.data.persistence.models.constructors.model
import tmg.flashback.data.persistence.models.drivers.Driver
import tmg.flashback.data.persistence.models.drivers.model

fun SprintRaceDriverResult.Companion.model(
    raceResult: SprintRaceResult = SprintRaceResult.model(),
    driver: Driver = Driver.model(),
    constructor: Constructor = Constructor.model()
): SprintRaceDriverResult = SprintRaceDriverResult(
    sprintResult = raceResult,
    driver = driver,
    constructor = constructor
)