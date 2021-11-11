package tmg.flashback.statistics.room.models.standings

import androidx.room.Embedded
import androidx.room.Relation
import tmg.flashback.statistics.room.models.constructors.Constructor

data class ConstructorStandingWithDrivers(
    @Embedded
    val standing: ConstructorStanding,
    @Relation(
        parentColumn = "constructor_id",
        entityColumn = "id"
    )
    val constructor: Constructor,
    @Relation(
        entity = ConstructorStandingDriver::class,
        parentColumn = "id",
        entityColumn = "constructor_season_id"
    )
    val drivers: List<ConstructorStandingDriverWithDriver>
) {
    companion object
}