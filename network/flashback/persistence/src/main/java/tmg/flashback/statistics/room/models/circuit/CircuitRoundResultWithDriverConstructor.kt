package tmg.flashback.statistics.room.models.circuit

import androidx.room.Embedded
import androidx.room.Relation
import tmg.flashback.statistics.room.models.constructors.Constructor
import tmg.flashback.statistics.room.models.drivers.Driver

data class CircuitRoundResultWithDriverConstructor(
    @Embedded
    val result: CircuitRoundResult,
    @Relation(
        parentColumn = "constructor_id",
        entityColumn = "id"
    )
    val constructor: Constructor,
    @Relation(
        parentColumn = "driver_id",
        entityColumn = "id"
    )
    val driver: Driver
) {
    companion object
}