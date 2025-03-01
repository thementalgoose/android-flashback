package tmg.flashback.data.persistence.models.race

import androidx.room.Embedded
import androidx.room.Relation
import tmg.flashback.data.persistence.models.circuit.Circuit
import tmg.flashback.data.persistence.models.overview.Schedule

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
    val schedule: List<Schedule>,
    @Relation(
        entity = SprintQualifyingResult::class,
        parentColumn = "id",
        entityColumn = "season_round_id"
    )
    val sprintQualifying: List<SprintQualifyingDriverResult>,
    @Relation(
        entity = SprintRaceResult::class,
        parentColumn = "id",
        entityColumn = "season_round_id"
    )
    val sprintRace: List<SprintRaceDriverResult>
) {
    companion object
}