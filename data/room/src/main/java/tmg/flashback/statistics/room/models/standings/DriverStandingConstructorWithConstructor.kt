package tmg.flashback.statistics.room.models.standings

import androidx.room.Embedded
import androidx.room.Relation
import tmg.flashback.statistics.room.models.constructors.Constructor

data class DriverStandingConstructorWithConstructor(
    @Embedded
    val standing: DriverStandingConstructor,
    @Relation(
        parentColumn = "constructor_id",
        entityColumn = "id"
    )
    val constructor: Constructor
) {
    companion object
}