package tmg.flashback.statistics.repo.dao

import androidx.room.Dao
import androidx.room.Query
import tmg.flashback.statistics.repo.models.round.Round

@Dao
interface SeasonDao {

    @Query("SELECT * FROM Round WHERE season == :season AND round == :round LIMIT 1")
    fun getRound(season: Int, round: Int): Round?
}