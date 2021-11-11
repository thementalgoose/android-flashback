package tmg.flashback.statistics.room.models.race

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RaceInfo(
    @ColumnInfo(name = "season")
    val season: Int,
    @ColumnInfo(name = "round")
    val round: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "circuit_id")
    val circuitId: String,
    @ColumnInfo(name = "time")
    val time: String?,
    @ColumnInfo(name = "wikiUrl")
    val wikiUrl: String?,
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = "${season}_${round}"
) {
    companion object
}