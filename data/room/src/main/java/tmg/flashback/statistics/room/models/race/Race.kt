package tmg.flashback.statistics.room.models.race

import androidx.room.Embedded
import androidx.room.Relation
import tmg.flashback.statistics.room.models.circuit.Circuit
import tmg.flashback.statistics.room.models.overview.Schedule

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
    @Relation(
        parentColumn = "id",
        entityColumn = "season_round_id"
    )
    val schedule: List<Schedule>
) {
    companion object
}