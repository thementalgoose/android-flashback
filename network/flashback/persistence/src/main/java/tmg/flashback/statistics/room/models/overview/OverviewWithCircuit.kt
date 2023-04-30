package tmg.flashback.statistics.room.models.overview

import androidx.room.Embedded
import androidx.room.Relation
import tmg.flashback.statistics.room.models.circuit.Circuit

data class OverviewWithCircuit(
    @Embedded
    val overview: Overview,
    @Relation(
        parentColumn = "circuit_id",
        entityColumn = "id"
    )
    val circuit: Circuit,
    @Relation(
        parentColumn = "id",
        entityColumn = "season_round_id"
    )
    val schedule: List<Schedule>
) {
    companion object
}