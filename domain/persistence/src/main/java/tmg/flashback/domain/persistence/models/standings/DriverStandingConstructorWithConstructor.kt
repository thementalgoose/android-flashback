package tmg.flashback.domain.persistence.models.standings

import androidx.room.Embedded
import androidx.room.Relation
import tmg.flashback.domain.persistence.models.constructors.Constructor

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