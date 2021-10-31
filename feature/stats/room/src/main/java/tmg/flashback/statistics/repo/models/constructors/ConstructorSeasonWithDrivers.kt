package tmg.flashback.statistics.repo.models.constructors

import androidx.room.Embedded
import androidx.room.Relation

data class ConstructorSeasonWithDrivers(
    @Embedded
    val constructorSeason: ConstructorSeason,
    @Relation(
        entity = ConstructorSeasonDriver::class,
        parentColumn = "id",
        entityColumn = "constructor_season_driver_id"
    )
    val drivers: List<ConstructorSeasonDriverWithDriver>
)