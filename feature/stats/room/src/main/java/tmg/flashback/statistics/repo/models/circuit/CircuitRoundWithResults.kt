package tmg.flashback.statistics.repo.models.circuit

import androidx.room.Embedded
import androidx.room.Relation

data class CircuitRoundWithResults(
    @Embedded
    val round: CircuitRound,
    @Relation(
        entity = CircuitRoundResult::class,
        parentColumn = "id",
        entityColumn = "season_round_position_id"
    )
    val results: List<CircuitRoundResultWithDriverConstructor>
)