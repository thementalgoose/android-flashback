package tmg.flashback.domain.persistence.models.race

import androidx.room.Embedded
import androidx.room.Relation
import tmg.flashback.domain.persistence.models.constructors.Constructor
import tmg.flashback.domain.persistence.models.drivers.Driver

class QualifyingDriverResult(
    @Embedded
    val qualifyingResult: QualifyingResult,
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