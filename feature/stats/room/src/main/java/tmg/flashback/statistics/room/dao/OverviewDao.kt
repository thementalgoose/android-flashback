package tmg.flashback.statistics.room.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import tmg.flashback.statistics.room.models.overview.Overview

@Dao
interface OverviewDao {

    @Query("SELECT * FROM overview WHERE season == :season AND round == :round LIMIT 1")
    fun getOverview(season: Int, round: Int): Flow<Overview?>

    @Query("SELECT * FROM overview WHERE season = :season")
    fun getOverview(season: Int): Flow<List<Overview?>>

    @Query("SELECT * FROM overview")
    fun getOverview(): Flow<List<Overview>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(overviews: List<Overview>)
}