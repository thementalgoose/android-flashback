package tmg.flashback.statistics.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import tmg.flashback.statistics.room.models.round.Round

@Dao
interface SeasonDao {

    @Query("SELECT * FROM Round WHERE season == :season AND round == :round LIMIT 1")
    fun getRound(season: Int, round: Int): Round?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRaceData(list: List<Round>)
}