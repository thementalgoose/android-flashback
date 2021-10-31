package tmg.flashback.statistics.room.models.circuit

import androidx.room.Embedded
import androidx.room.Relation
import tmg.flashback.statistics.room.models.constructors.Constructor
import tmg.flashback.statistics.room.models.drivers.Driver

data class CircuitRoundResultWithDriverConstructor(
    @Embedded
    val result: CircuitRoundResult,
    @Relation(
        parentColumn = "id",
        entityColumn = "constructor_id"
    )
    val constructor: Constructor,
    @Relation(
        parentColumn = "id",
        entityColumn = "driver_id"
    )
    val driver: Driver
)