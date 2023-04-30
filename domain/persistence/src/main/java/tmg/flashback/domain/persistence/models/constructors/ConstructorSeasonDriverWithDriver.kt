package tmg.flashback.domain.persistence.models.constructors

import androidx.room.Embedded
import androidx.room.Relation
import tmg.flashback.domain.persistence.models.drivers.Driver

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