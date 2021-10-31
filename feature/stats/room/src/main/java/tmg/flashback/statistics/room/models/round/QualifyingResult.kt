package tmg.flashback.statistics.room.models.round

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class QualifyingResult(
    @ColumnInfo(name = "driver_id")
    val driverId: String,
    @ColumnInfo(name = "season")
    val season: Int,
    @ColumnInfo(name = "round")
    val round: Int,
    @ColumnInfo(name = "constructorId")
    val constructorId: String,
    @ColumnInfo(name = "qualified")
    val qualified: Int?,
    @ColumnInfo(name = "q1")
    val q1: String?,
    @ColumnInfo(name = "q2")
    val q2: String?,
    @ColumnInfo(name = "q3")
    val q3: String?,
    @Embedded
    val qSprint: QualifyingSprintResult?,
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = "${driverId}_${season}_${round}"
)

