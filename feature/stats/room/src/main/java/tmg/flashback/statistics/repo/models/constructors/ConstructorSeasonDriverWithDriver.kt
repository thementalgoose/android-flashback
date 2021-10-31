package tmg.flashback.statistics.repo.models.constructors

import androidx.room.Embedded
import androidx.room.Relation
import tmg.flashback.statistics.repo.models.drivers.Driver

data class ConstructorSeasonDriverWithDriver(
    @Embedded
    val results: ConstructorSeasonDriver,
    @Relation(
        parentColumn = "id",
        entityColumn = "driver_id"
    )
    val driver: Driver
)