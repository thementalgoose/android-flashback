package tmg.flashback.statistics.room.models.standings

import androidx.room.Embedded
import androidx.room.Relation
import tmg.flashback.statistics.room.models.drivers.Driver

data class ConstructorStandingDriverWithDriver(
    @Embedded
    val standing: ConstructorStandingDriver,
    @Relation(
        parentColumn = "driver_id",
        entityColumn = "id"
    )
    val driver: Driver
)