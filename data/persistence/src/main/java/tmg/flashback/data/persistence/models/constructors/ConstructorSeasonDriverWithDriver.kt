package tmg.flashback.data.persistence.models.constructors

import androidx.room.Embedded
import androidx.room.Relation
import tmg.flashback.data.persistence.models.drivers.Driver

data class ConstructorSeasonDriverWithDriver(
    @Embedded
    val results: ConstructorSeasonDriver,
    @Relation(
        parentColumn = "driver_id",
        entityColumn = "id"
    )
    val driver: Driver
) {
    companion object
}