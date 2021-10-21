package tmg.flashback.data.db.stats

import kotlinx.coroutines.flow.Flow
import tmg.flashback.data.models.stats.*

interface SeasonOverviewRepository {
    fun getCircuits(season: Int): Flow<List<CircuitSummary>>
    fun getCircuit(season: Int, round: Int): Flow<CircuitSummary?>
    fun getConstructor(season: Int, constructorId: String): Flow<Constructor?>
    fun getDriver(season: Int, driver: String): Flow<Driver?>
    fun getAllConstructors(season: Int): Flow<List<Constructor>>
    fun getSeasonOverview(season: Int): Flow<Season>
    fun getSeasonRound(season: Int, round: Int): Flow<Round?>
}