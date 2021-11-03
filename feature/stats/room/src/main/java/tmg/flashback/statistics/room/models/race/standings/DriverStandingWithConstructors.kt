package tmg.flashback.statistics.room.models.race.standings

import androidx.room.Embedded
import androidx.room.Relation
import tmg.flashback.statistics.room.models.drivers.Driver

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
)