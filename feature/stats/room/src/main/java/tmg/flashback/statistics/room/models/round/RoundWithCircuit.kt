package tmg.flashback.statistics.room.models.round

import androidx.room.Embedded
import androidx.room.Relation
import tmg.flashback.statistics.room.models.circuit.Circuit

data class RoundWithCircuit(
    @Embedded
    val round: Round,
    @Relation(
        parentColumn = "circuit_id",
        entityColumn = "id"
    )
    val circuit: Circuit
)