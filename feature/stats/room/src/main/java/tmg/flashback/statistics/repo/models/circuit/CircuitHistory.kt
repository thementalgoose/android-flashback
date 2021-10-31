package tmg.flashback.statistics.repo.models.circuit

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Relation

data class CircuitHistory(
    @Embedded
    val circuit: Circuit,
    @Relation(
        entity = CircuitRound::class,
        parentColumn = "id",
        entityColumn = "circuit_season_round_id"
    )
    val races: List<CircuitRoundWithResults>
)