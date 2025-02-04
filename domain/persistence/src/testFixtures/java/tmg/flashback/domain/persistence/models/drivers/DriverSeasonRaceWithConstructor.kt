package tmg.flashback.domain.persistence.models.drivers

import tmg.flashback.domain.persistence.models.constructors.Constructor
import tmg.flashback.domain.persistence.models.constructors.model
import tmg.flashback.domain.persistence.models.race.RaceInfoWithCircuit
import tmg.flashback.domain.persistence.models.race.model

fun DriverSeasonRaceWithConstructor.Companion.model(
    race: DriverSeasonRace = DriverSeasonRace.model(),
    constructor: Constructor = Constructor.model(),
    round: RaceInfoWithCircuit = RaceInfoWithCircuit.model()
): DriverSeasonRaceWithConstructor = DriverSeasonRaceWithConstructor(
    race = race,
    constructor = constructor,
    round = round
)