package tmg.flashback.statistics.repo.models.circuit

import androidx.room.Embedded
import androidx.room.Relation
import tmg.flashback.statistics.repo.models.constructors.Constructor
import tmg.flashback.statistics.repo.models.drivers.Driver

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