package tmg.flashback.statistics.room.models.race

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WinterTesting(
    @ColumnInfo(name = "season")
    val season: Int,
    @ColumnInfo(name = "label")
    val label: String,
    @ColumnInfo(name = "date")
    val date: String,
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = "$season-$date"
) {
    companion object
}