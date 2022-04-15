package tmg.flashback.statistics.room.models.race

import tmg.flashback.statistics.room.models.constructors.Constructor
import tmg.flashback.statistics.room.models.constructors.model
import tmg.flashback.statistics.room.models.drivers.Driver
import tmg.flashback.statistics.room.models.drivers.model

fun SprintDriverResult.Companion.model(
    raceResult: SprintResult = SprintResult.model(),
    driver: Driver = Driver.model(),
    constructor: Constructor = Constructor.model()
): SprintDriverResult = SprintDriverResult(
    sprintResult = raceResult,
    driver = driver,
    constructor = constructor
)