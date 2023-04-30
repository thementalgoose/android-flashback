package tmg.flashback.domain.persistence.models.race

import tmg.flashback.domain.persistence.models.constructors.Constructor
import tmg.flashback.domain.persistence.models.constructors.model
import tmg.flashback.domain.persistence.models.drivers.Driver
import tmg.flashback.domain.persistence.models.drivers.model

fun SprintQualifyingDriverResult.Companion.model(
    qualifyingResult: SprintQualifyingResult = SprintQualifyingResult.model(),
    driver: Driver = Driver.model(),
    constructor: Constructor = Constructor.model()
): SprintQualifyingDriverResult = SprintQualifyingDriverResult(
    qualifyingResult = qualifyingResult,
    driver = driver,
    constructor = constructor
)