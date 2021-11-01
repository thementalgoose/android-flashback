package tmg.flashback.statistics.room.models.drivers

import androidx.room.Embedded
import androidx.room.Relation
import tmg.flashback.statistics.room.models.constructors.Constructor
import tmg.flashback.statistics.room.models.round.Round
import tmg.flashback.statistics.room.models.round.RoundWithCircuit

data class DriverSeasonRaceWithConstructor(
    @Embedded
    val race: DriverSeasonRace,
    @Relation(
        parentColumn = "constructor_id",
        entityColumn = "id"
    )
    val constructor: Constructor,
    @Relation(
        entity = Round::class,
        parentColumn = "season_round_id",
        entityColumn = "id"
    )
    val round: RoundWithCircuit
)