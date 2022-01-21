package tmg.flashback.statistics.room.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalDate
import tmg.flashback.statistics.room.models.overview.OverviewWithCircuit
import tmg.flashback.statistics.room.models.overview.Schedule
import tmg.flashback.statistics.room.models.overview.WinterTesting
import tmg.utilities.extensions.format

@Dao
interface ScheduleDao {

    @Transaction
    @Query("SELECT * FROM Overview WHERE date >= :fromDate")
    suspend fun getUpcomingEvents(fromDate: String): List<OverviewWithCircuit>

    @Transaction
    suspend fun getUpcomingEvents(fromDate: LocalDate): List<OverviewWithCircuit> {
        val fromDateString = fromDate.format("yyyy-MM-dd") ?: return emptyList()
        return getUpcomingEvents(fromDateString)
    }

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

    //region Winter Testing

    @Query("SELECT * FROM WinterTesting WHERE season == :season")
    fun getWinterTesting(season: Int): Flow<List<WinterTesting>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWinterTesting(winterTesting: List<WinterTesting>)

    @Query("DELETE FROM WinterTesting WHERE season == :season")
    fun deleteWinterTestingForSeason(season: Int)

    @Transaction
    fun replaceWinterTestingForSeason(season: Int, testing: List<WinterTesting>) {
        deleteWinterTestingForSeason(season)
        insertWinterTesting(testing)
    }

    //endregion
}