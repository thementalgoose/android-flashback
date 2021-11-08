package tmg.flashback.statistics.room.dao

import androidx.room.*
import tmg.flashback.statistics.room.models.overview.Schedule

@Dao
interface ScheduleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(schedule: Schedule)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(schedule: List<Schedule>)

    @Transaction
    fun replaceAllForRace(season: Int, round: Int, schedule: List<Schedule>) {
        deleteForRace(season, round)
        insertAll(schedule)
    }

    @Transaction
    fun replaceAllForSeason(season: Int, schedule: List<Schedule>) {
        deleteForSeason(season)
        insertAll(schedule)
    }

    @Query("DELETE FROM Schedule WHERE season == :season AND round == :round")
    fun deleteForRace(season: Int, round: Int)

    @Query("DELETE FROM Schedule WHERE season == :season")
    fun deleteForSeason(season: Int)
}