package tmg.flashback.data.persistence.models.standings

import androidx.room.Embedded
import androidx.room.Relation
import tmg.flashback.data.persistence.models.constructors.Constructor

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