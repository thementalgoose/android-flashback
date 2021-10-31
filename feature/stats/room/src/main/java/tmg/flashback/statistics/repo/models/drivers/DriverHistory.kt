package tmg.flashback.statistics.repo.models.drivers

import androidx.room.*

data class DriverHistory(
    @Embedded
    val driver: Driver,
    @Relation(
        entity = DriverSeason::class,
        parentColumn = "id",
        entityColumn = "season_id"
    )
    val seasons: List<DriverSeasonWithRaces>
)