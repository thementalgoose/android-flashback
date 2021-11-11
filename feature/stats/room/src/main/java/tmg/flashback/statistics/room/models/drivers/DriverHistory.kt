package tmg.flashback.statistics.room.models.drivers

import androidx.room.*

data class DriverHistory(
    @Embedded
    val driver: Driver,
    @Relation(
        entity = DriverSeason::class,
        parentColumn = "id",
        entityColumn = "driver_id"
    )
    val seasons: List<DriverSeasonWithRaces>
) {
    companion object
}