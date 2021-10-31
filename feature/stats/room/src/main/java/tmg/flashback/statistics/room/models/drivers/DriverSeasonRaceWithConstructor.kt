package tmg.flashback.statistics.room.models.drivers

import androidx.room.Embedded
import androidx.room.Relation
import tmg.flashback.statistics.room.models.constructors.Constructor

data class DriverSeasonRaceWithConstructor(
    @Embedded
    val race: DriverSeasonRace,
    @Relation(
        parentColumn = "id",
        entityColumn = "constructor_id"
    )
    val constructor: Constructor,
)