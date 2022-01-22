package tmg.flashback.statistics.room.models.race

import tmg.flashback.statistics.room.models.constructors.Constructor
import tmg.flashback.statistics.room.models.constructors.model
import tmg.flashback.statistics.room.models.drivers.Driver
import tmg.flashback.statistics.room.models.drivers.model
import tmg.flashback.statistics.room.models.overview.model

fun QualifyingDriverResult.Companion.model(
    qualifyingResult: QualifyingResult = QualifyingResult.model(),
    driver: Driver = Driver.model(),
    constructor: Constructor = Constructor.model()
): QualifyingDriverResult = QualifyingDriverResult(
    qualifyingResult = qualifyingResult,
    driver = driver,
    constructor = constructor
)