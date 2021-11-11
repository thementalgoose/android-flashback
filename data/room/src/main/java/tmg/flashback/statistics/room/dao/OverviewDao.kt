package tmg.flashback.statistics.room.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import tmg.flashback.statistics.room.models.overview.Overview
import tmg.flashback.statistics.room.models.overview.OverviewWithCircuit

@Dao
interface OverviewDao {

    @Transaction
    @Query("SELECT * FROM overview WHERE season == :season AND round == :round LIMIT 1")
    fun getOverview(season: Int, round: Int): Flow<OverviewWithCircuit?>

    @Transaction
    @Query("SELECT * FROM overview WHERE season = :season")
    fun getOverview(season: Int): Flow<List<OverviewWithCircuit?>>

    @Transaction
    @Query("SELECT * FROM overview")
    fun getOverview(): Flow<List<OverviewWithCircuit>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(overviews: List<Overview>)
}