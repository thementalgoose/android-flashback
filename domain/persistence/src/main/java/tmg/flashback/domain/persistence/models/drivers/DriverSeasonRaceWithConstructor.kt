package tmg.flashback.domain.persistence.models.drivers

import androidx.room.Embedded
import androidx.room.Relation
import tmg.flashback.domain.persistence.models.constructors.Constructor
import tmg.flashback.domain.persistence.models.race.RaceInfo
import tmg.flashback.domain.persistence.models.race.RaceInfoWithCircuit

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
) {
    companion object
}