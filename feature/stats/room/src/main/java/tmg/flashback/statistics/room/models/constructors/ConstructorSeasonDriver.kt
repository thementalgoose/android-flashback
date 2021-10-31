package tmg.flashback.statistics.room.models.constructors

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ConstructorSeasonDriver(
    @ColumnInfo(name = "constructor_id")
    val constructorId: Int,
    @ColumnInfo(name = "season")
    val season: Int,
    @ColumnInfo(name = "driver_id")
    val driverId: String,
    @ColumnInfo(name = "points")
    val points: Double,
    @ColumnInfo(name = "championship_position")
    val championshipPosition: Int,
    @ColumnInfo(name = "wins")
    val wins: Int,
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = "${constructorId}_${season}_${driverId}"
)