package tmg.flashback.domain.persistence.models.race

import androidx.room.ColumnInfo

data class FastestLap(
    @ColumnInfo(name = "position")
    val position: Int,
    @ColumnInfo(name = "time")
    val time: String
) {
    companion object
}