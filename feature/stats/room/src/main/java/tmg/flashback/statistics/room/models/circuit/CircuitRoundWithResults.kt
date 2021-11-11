package tmg.flashback.statistics.room.models.circuit

import androidx.room.Embedded
import androidx.room.Relation

data class CircuitRoundWithResults(
    @Embedded
    val round: CircuitRound,
    @Relation(
        entity = CircuitRoundResult::class,
        parentColumn = "id",
        entityColumn = "id"
    )
    val results: List<CircuitRoundResultWithDriverConstructor>
) {
    companion object
}