package tmg.flashback.statistics.repo.dao

import androidx.room.*
import tmg.flashback.statistics.repo.models.overview.Overview

@Dao
interface OverviewDao {

    @Query("SELECT * FROM overview WHERE season == :season AND round == :round LIMIT 1")
    fun getOverview(season: Int, round: Int): Overview?

    @Query("SELECT * FROM overview WHERE season = :season")
    fun getOverview(season: Int): List<Overview>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg overviews: Overview)
}