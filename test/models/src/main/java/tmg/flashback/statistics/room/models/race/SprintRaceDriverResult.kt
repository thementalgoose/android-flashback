package tmg.flashback.statistics.room.models.race

import tmg.flashback.statistics.room.models.constructors.Constructor
import tmg.flashback.statistics.room.models.constructors.model
import tmg.flashback.statistics.room.models.drivers.Driver
import tmg.flashback.statistics.room.models.drivers.model

fun SprintRaceDriverResult.Companion.model(
    raceResult: SprintRaceResult = SprintRaceResult.model(),
    driver: Driver = Driver.model(),
    constructor: Constructor = Constructor.model()
): SprintRaceDriverResult = SprintRaceDriverResult(
    sprintResult = raceResult,
    driver = driver,
    constructor = constructor
)