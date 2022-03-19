package tmg.flashback.statistics.room.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import tmg.flashback.statistics.room.models.race.*

@Dao
interface SeasonDao {

    @Transaction
    @Query("SELECT * FROM RaceInfo WHERE season == :season")
    fun getRaces(season: Int): Flow<List<Race>>

    @Query("SELECT * FROM RaceInfo WHERE season == :season AND round == :round LIMIT 1")
    fun getRaceInfo(season: Int, round: Int): RaceInfo?

    @Query("SELECT COUNT(*) FROM RaceInfo WHERE season == :season")
    suspend fun getRaceCount(season: Int): Int

    @Query("SELECT COUNT(*) FROM RaceInfo WHERE season == :season AND round == :round")
    suspend fun getRaceCount(season: Int, round: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRaceData(list: List<RaceInfo>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRaceData(list: RaceInfo)

    @Transaction
    fun insertRace(
        race: RaceInfo,
        qualifyingResults: List<QualifyingResult>,
        sprintResults: List<SprintResult>,
        raceResults: List<RaceResult>
    ) {
        insertQualifyingResults(qualifyingResults)
        insertSprintResults(sprintResults)
        insertRaceResult(raceResults)
        insertRaceData(race)
    }

    @Transaction
    fun insertRaces(
        races: List<RaceInfo>,
        qualifyingResults: List<QualifyingResult>,
        sprintResults: List<SprintResult>,
        raceResults: List<RaceResult>,
    ) {
        insertQualifyingResults(qualifyingResults)
        insertSprintResults(sprintResults)
        insertRaceResult(raceResults)
        insertRaceData(races)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertQualifyingResults(results: List<QualifyingResult>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSprintResults(results: List<SprintResult>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRaceResult(results: List<RaceResult>)

    @Transaction
    @Query("SELECT * FROM RaceInfo WHERE season == :season AND round == :round LIMIT 1")
    fun getRace(season: Int, round: Int): Flow<Race?>
}