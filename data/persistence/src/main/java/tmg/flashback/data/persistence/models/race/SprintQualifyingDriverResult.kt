package tmg.flashback.data.persistence.models.race

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation
import tmg.flashback.data.persistence.models.constructors.Constructor
import tmg.flashback.data.persistence.models.drivers.Driver

@Entity
data class SprintQualifyingDriverResult(
    @Embedded
    val qualifyingResult: SprintQualifyingResult,
    @Relation(
        parentColumn = "driver_id",
        entityColumn = "id"
    )
    val driver: Driver,
    @Relation(
        parentColumn = "constructor_id",
        entityColumn = "id"
    )
    val constructor: Constructor
) {
    companion object
}