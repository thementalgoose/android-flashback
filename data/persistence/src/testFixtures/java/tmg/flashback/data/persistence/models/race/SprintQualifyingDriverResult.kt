package tmg.flashback.data.persistence.models.race

import tmg.flashback.data.persistence.models.constructors.Constructor
import tmg.flashback.data.persistence.models.constructors.model
import tmg.flashback.data.persistence.models.drivers.Driver
import tmg.flashback.data.persistence.models.drivers.model

fun SprintQualifyingDriverResult.Companion.model(
    qualifyingResult: SprintQualifyingResult = SprintQualifyingResult.model(),
    driver: Driver = Driver.model(),
    constructor: Constructor = Constructor.model()
): SprintQualifyingDriverResult = SprintQualifyingDriverResult(
    qualifyingResult = qualifyingResult,
    driver = driver,
    constructor = constructor
)