package tmg.flashback.data.persistence.models.standings

import androidx.room.Embedded
import androidx.room.Relation
import tmg.flashback.data.persistence.models.drivers.Driver

data class ConstructorStandingDriverWithDriver(
    @Embedded
    val standing: ConstructorStandingDriver,
    @Relation(
        parentColumn = "driver_id",
        entityColumn = "id"
    )
    val driver: Driver
) {
    companion object
}