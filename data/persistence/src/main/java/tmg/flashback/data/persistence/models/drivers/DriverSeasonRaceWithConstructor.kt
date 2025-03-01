package tmg.flashback.data.persistence.models.drivers

import androidx.room.Embedded
import androidx.room.Relation
import tmg.flashback.data.persistence.models.constructors.Constructor
import tmg.flashback.data.persistence.models.race.RaceInfo
import tmg.flashback.data.persistence.models.race.RaceInfoWithCircuit

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