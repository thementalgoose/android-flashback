package tmg.flashback.statistics.room.models.drivers

import androidx.room.Embedded
import androidx.room.Relation
import tmg.flashback.statistics.room.models.constructors.Constructor
import tmg.flashback.statistics.room.models.race.RaceInfo
import tmg.flashback.statistics.room.models.race.RaceInfoWithCircuit

data class DriverSeasonRaceWithConstructor(
    @Embedded
    val race: DriverSeasonRace,
    @Relation(
        parentColumn = "constructor_id",
        entityColumn = "id"
    )
    val constructor: Constructor,
    @Relation(
        entity = RaceInfo::class,
        parentColumn = "season_round_id",
        entityColumn = "id"
    )
    val round: RaceInfoWithCircuit
)