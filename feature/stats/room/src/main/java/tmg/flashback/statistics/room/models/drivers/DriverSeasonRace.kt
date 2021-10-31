package tmg.flashback.statistics.room.models.drivers

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class DriverSeasonRace(
    @ColumnInfo(name = "driver_id")
    val driverId: String,
    @ColumnInfo(name = "season")
    val season: Int,
    @ColumnInfo(name = "round")
    val round: Int,
    @ColumnInfo(name = "constructor_id")
    val constructorId: String,
    @ColumnInfo(name = "round_id")
    val roundId: String,
    @ColumnInfo(name = "is_sprint_quali")
    val sprintQuali: Boolean,
    @ColumnInfo(name = "qualified")
    val qualified: Int?,
    @ColumnInfo(name = "gridPos")
    val gridPos: Int,
    @ColumnInfo(name = "finished")
    val finished: Int,
    @ColumnInfo(name = "status")
    val status: String,
    @ColumnInfo(name = "points")
    val points: Double,
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = "${driverId}_${season}_${round}"
)