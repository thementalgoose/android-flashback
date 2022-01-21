package tmg.flashback.statistics.room.models.drivers

import tmg.flashback.statistics.room.models.constructors.Constructor
import tmg.flashback.statistics.room.models.race.RaceInfoWithCircuit
import tmg.flashback.statistics.room.models.overview.model

fun DriverSeasonRaceWithConstructor.Companion.model(
    race: DriverSeasonRace = DriverSeasonRace.model(),
    constructor: Constructor = Constructor.model(),
    round: RaceInfoWithCircuit = RaceInfoWithCircuit.model()
): DriverSeasonRaceWithConstructor = DriverSeasonRaceWithConstructor(
    race = race,
    constructor = constructor,
    round = round
)