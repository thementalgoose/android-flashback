package tmg.flashback.domain.persistence.models.standings

import androidx.room.Embedded
import androidx.room.Relation
import tmg.flashback.domain.persistence.models.drivers.Driver

data class DriverStandingWithConstructors(
    @Embedded
    val standing: DriverStanding,
    @Relation(
        parentColumn = "driver_id",
        entityColumn = "id"
    )
    val driver: Driver,
    @Relation(
        entity = DriverStandingConstructor::class,
        parentColumn = "id",
        entityColumn = "driver_season_id"
    )
    val constructors: List<DriverStandingConstructorWithConstructor>
) {
    companion object
}