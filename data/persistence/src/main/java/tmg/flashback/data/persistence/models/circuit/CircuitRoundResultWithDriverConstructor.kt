package tmg.flashback.data.persistence.models.circuit

import androidx.room.Embedded
import androidx.room.Relation
import tmg.flashback.data.persistence.models.constructors.Constructor
import tmg.flashback.data.persistence.models.drivers.Driver

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