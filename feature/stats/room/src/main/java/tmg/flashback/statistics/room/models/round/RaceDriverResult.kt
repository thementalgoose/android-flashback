package tmg.flashback.statistics.room.models.round

import androidx.room.Embedded
import androidx.room.Relation
import tmg.flashback.statistics.room.models.constructors.Constructor
import tmg.flashback.statistics.room.models.drivers.Driver

data class RaceDriverResult(
    @Embedded
    val raceResult: RaceResult,
    @Relation(
        parentColumn = "id",
        entityColumn = "driver_id"
    )
    val driver: Driver,
    @Relation(
        parentColumn = "id",
        entityColumn = "constructor_id"
    )
    val constructor: Constructor
)