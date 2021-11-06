package tmg.flashback.statistics.room.models.race

import androidx.room.*
import tmg.flashback.statistics.room.models.circuit.Circuit

data class Race(
    @Embedded
    val raceInfo: RaceInfo,
    @Relation(
        parentColumn = "circuit_id",
        entityColumn = "id"
    )
    val circuit: Circuit,
    @Relation(
        entity = QualifyingResult::class,
        parentColumn = "id",
        entityColumn = "season_round_id"
    )
    val qualifying: List<QualifyingDriverResult>,
    @Relation(
        entity = RaceResult::class,
        parentColumn = "id",
        entityColumn = "season_round_id"
    )
    val race: List<RaceDriverResult>,
) {
    companion object
}