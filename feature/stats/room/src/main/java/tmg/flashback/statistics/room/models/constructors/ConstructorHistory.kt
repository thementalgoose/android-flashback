package tmg.flashback.statistics.room.models.constructors

import androidx.room.*

data class ConstructorHistory(
    @Embedded
    val constructor: Constructor,
    @Relation(
        entity = ConstructorSeason::class,
        parentColumn = "id",
        entityColumn = "constructor_id"
    )
    val seasons: List<ConstructorSeasonWithDrivers>
) {
    companion object
}