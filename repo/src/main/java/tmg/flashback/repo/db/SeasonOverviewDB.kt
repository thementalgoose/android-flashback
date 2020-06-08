package tmg.flashback.repo.db

import kotlinx.coroutines.flow.Flow
import tmg.flashback.repo.models.Circuit
import tmg.flashback.repo.models.Constructor
import tmg.flashback.repo.models.Driver
import tmg.flashback.repo.models.Round

interface SeasonOverviewDB {
    suspend fun getCircuits(season: Int): Flow<List<Circuit>>
    suspend fun getCircuit(season: Int, round: Int): Flow<Circuit?>
    suspend fun getConstructor(season: Int, constructorId: String): Flow<Constructor?>
    suspend fun getDriver(season: Int, driver: String): Flow<Driver?>
    suspend fun getAllConstructors(season: Int): Flow<List<Constructor>>
    suspend fun getSeasonOverview(season: Int): Flow<Pair<Int, List<Round>>>
    suspend fun getPreviousWeekend(season: Int): Flow<Round?> // null = first weekend
    suspend fun getNextWeekend(season: Int): Flow<Round?> // null = last weekend
    suspend fun getSeasonRound(season: Int, round: Int): Flow<Round?>
}