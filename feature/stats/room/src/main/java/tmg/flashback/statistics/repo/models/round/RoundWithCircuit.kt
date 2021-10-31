package tmg.flashback.statistics.repo.models.round

import androidx.room.Embedded
import androidx.room.Relation
import tmg.flashback.statistics.repo.models.circuit.Circuit

data class RoundWithCircuit(
    @Embedded
    val round: Round,
    @Relation(
        parentColumn = "id",
        entityColumn = "circuit_id"
    )
    val circuit: Circuit
)