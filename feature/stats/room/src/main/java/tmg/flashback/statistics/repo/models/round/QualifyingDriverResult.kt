package tmg.flashback.statistics.repo.models.round

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.PrimaryKey
import androidx.room.Relation
import tmg.flashback.statistics.repo.models.constructors.Constructor
import tmg.flashback.statistics.repo.models.drivers.Driver

class QualifyingDriverResult(
    @Embedded
    val raceResult: QualifyingResult,
    @Relation(
        parentColumn = "id",
        entityColumn = "driver_id"
    )
    val driver: Driver,
    @Relation(
        parentColumn = "id",
        entityColumn = "constructor_id"
    )
    val constructor: Constructor
)