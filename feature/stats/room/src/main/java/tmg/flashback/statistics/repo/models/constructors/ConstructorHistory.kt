package tmg.flashback.statistics.repo.models.constructors

import androidx.room.*

data class ConstructorHistory(
    @Embedded
    val constructor: Constructor,
    @Relation(
        entity = ConstructorSeason::class,
        parentColumn = "id",
        entityColumn = "constructor_season_id"
    )
    val seasons: List<ConstructorSeason>
)