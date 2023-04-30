package tmg.flashback.statistics.room.models.overview

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Schedule(
    @ColumnInfo(name = "season")
    val season: Int,
    @ColumnInfo(name = "round")
    val round: Int,
    @ColumnInfo(name = "label")
    val label: String,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "time")
    val time: String,
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = "${season}_${round}_${label}",
    @ColumnInfo(name = "season_round_id")
    val seasonRoundId: String = "${season}_${round}"
) {
    companion object
}