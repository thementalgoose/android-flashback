package tmg.flashback.data.persistence.models.drivers

import tmg.flashback.data.persistence.models.constructors.Constructor
import tmg.flashback.data.persistence.models.constructors.model
import tmg.flashback.data.persistence.models.race.RaceInfoWithCircuit
import tmg.flashback.data.persistence.models.race.model

fun DriverSeasonRaceWithConstructor.Companion.model(
    race: DriverSeasonRace = DriverSeasonRace.model(),
    constructor: Constructor = Constructor.model(),
    round: RaceInfoWithCircuit = RaceInfoWithCircuit.model()
): DriverSeasonRaceWithConstructor = DriverSeasonRaceWithConstructor(
    race = race,
    constructor = constructor,
    round = round
)