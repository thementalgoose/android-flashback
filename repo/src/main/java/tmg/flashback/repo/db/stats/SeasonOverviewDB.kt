package tmg.flashback.repo.db.stats

import kotlinx.coroutines.flow.Flow
import tmg.flashback.repo.models.stats.CircuitSummary
import tmg.flashback.repo.models.stats.Constructor
import tmg.flashback.repo.models.stats.Driver
import tmg.flashback.repo.models.stats.Round

interface SeasonOverviewDB {
    suspend fun getCircuits(season: Int): Flow<List<CircuitSummary>>
    suspend fun getCircuit(season: Int, round: Int): Flow<CircuitSummary?>
    suspend fun getConstructor(season: Int, constructorId: String): Flow<Constructor?>
    suspend fun getDriver(season: Int, driver: String): Flow<Driver?>
    suspend fun getAllConstructors(season: Int): Flow<List<Constructor>>
    suspend fun getSeasonOverview(season: Int): Flow<Pair<Int, List<Round>>>
    suspend fun getPreviousWeekend(season: Int): Flow<Round?> // null = first weekend
    suspend fun getNextWeekend(season: Int): Flow<Round?> // null = last weekend
    suspend fun getSeasonRound(season: Int, round: Int): Flow<Round?>
}