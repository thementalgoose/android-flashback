package tmg.flashback.statistics.room.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import tmg.flashback.statistics.room.models.race.QualifyingResult
import tmg.flashback.statistics.room.models.race.Race
import tmg.flashback.statistics.room.models.race.RaceInfo
import tmg.flashback.statistics.room.models.race.RaceResult

@Dao
interface SeasonDao {

    @Query("SELECT * FROM RaceInfo WHERE season == :season AND round == :round LIMIT 1")
    fun getRaceInfo(season: Int, round: Int): RaceInfo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRaceData(list: List<RaceInfo>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRaceData(list: RaceInfo)

    @Transaction
    fun insertRace(race: RaceInfo, qualifyingResults: List<QualifyingResult>, raceResults: List<RaceResult>) {
        insertQualifyingResults(qualifyingResults)
        insertRaceResult(raceResults)
        insertRaceData(race)
    }

    @Transaction
    fun insertRaces(races: List<RaceInfo>, qualifyingResults: List<QualifyingResult>, raceResults: List<RaceResult>) {
        insertQualifyingResults(qualifyingResults)
        insertRaceResult(raceResults)
        insertRaceData(races)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertQualifyingResults(results: List<QualifyingResult>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRaceResult(results: List<RaceResult>)

    @Transaction
    @Query("SELECT * FROM RaceInfo WHERE season == :season AND round == :round LIMIT 1")
    fun getRace(season: Int, round: Int): Flow<Race?>
}