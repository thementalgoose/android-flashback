package tmg.flashback.statistics.repo.models.round

import androidx.room.ColumnInfo
import androidx.room.Entity

data class FastestLap(
    @ColumnInfo(name = "position")
    val position: Int,
    @ColumnInfo(name = "time")
    val time: String
)