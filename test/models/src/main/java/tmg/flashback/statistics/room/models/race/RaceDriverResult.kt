package tmg.flashback.statistics.room.models.race

import tmg.flashback.statistics.room.models.constructors.Constructor
import tmg.flashback.statistics.room.models.constructors.model
import tmg.flashback.statistics.room.models.drivers.Driver
import tmg.flashback.statistics.room.models.drivers.model

fun RaceDriverResult.Companion.model(
    raceResult: RaceResult = RaceResult.model(),
    driver: Driver = Driver.model(),
    constructor: Constructor = Constructor.model()
): RaceDriverResult = RaceDriverResult(
    raceResult = raceResult,
    driver = driver,
    constructor = constructor
)