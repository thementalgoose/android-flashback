package tmg.flashback.statistics.room.models.constructors

import androidx.room.Embedded
import androidx.room.Relation
import tmg.flashback.statistics.room.models.drivers.Driver

data class ConstructorSeasonDriverWithDriver(
    @Embedded
    val results: ConstructorSeasonDriver,
    @Relation(
        parentColumn = "id",
        entityColumn = "driver_id"
    )
    val driver: Driver
)