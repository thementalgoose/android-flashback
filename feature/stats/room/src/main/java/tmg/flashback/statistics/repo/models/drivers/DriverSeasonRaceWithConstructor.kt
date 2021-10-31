package tmg.flashback.statistics.repo.models.drivers

import androidx.room.Embedded
import androidx.room.Relation
import tmg.flashback.statistics.repo.models.constructors.Constructor

data class DriverSeasonRaceWithConstructor(
    @Embedded
    val race: DriverSeasonRace,
    @Relation(
        parentColumn = "id",
        entityColumn = "constructor_id"
    )
    val constructor: Constructor,
)