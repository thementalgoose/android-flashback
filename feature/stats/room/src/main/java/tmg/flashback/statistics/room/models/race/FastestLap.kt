package tmg.flashback.statistics.room.models.race

import androidx.room.ColumnInfo

data class FastestLap(
    @ColumnInfo(name = "position")
    val position: Int,
    @ColumnInfo(name = "time")
    val time: String
)