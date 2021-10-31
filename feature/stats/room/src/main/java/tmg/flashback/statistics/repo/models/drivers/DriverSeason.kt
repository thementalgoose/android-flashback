package tmg.flashback.statistics.repo.models.drivers

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DriverSeason(
    @ColumnInfo(name = "driverId")
    val driverId: String,
    @ColumnInfo(name = "season")
    val season: Int,
    @ColumnInfo(name = "championship_standing")
    val championshipStanding: Int?,
    @ColumnInfo(name = "points")
    val points: Double,
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = "${driverId}_${season}"
)