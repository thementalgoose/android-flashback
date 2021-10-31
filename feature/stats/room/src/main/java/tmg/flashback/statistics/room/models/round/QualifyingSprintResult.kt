package tmg.flashback.statistics.room.models.round

import androidx.room.ColumnInfo

data class QualifyingSprintResult(
    @ColumnInfo(name = "points")
    val points: Double,
    @ColumnInfo(name = "grid_position")
    val gridPos: Int?,
    @ColumnInfo(name = "finished")
    val finished: Int,
    @ColumnInfo(name = "status")
    val status: String,
    @ColumnInfo(name = "time")
    val time: String
)