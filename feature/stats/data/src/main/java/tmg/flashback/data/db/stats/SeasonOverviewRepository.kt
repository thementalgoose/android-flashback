package tmg.flashback.data.db.stats

import kotlinx.coroutines.flow.Flow
import tmg.flashback.data.models.stats.*

interface SeasonOverviewRepository {
    fun getCircuits(season: Int): Flow<List<tmg.flashback.formula1.model.CircuitSummary>>
    fun getCircuit(season: Int, round: Int): Flow<tmg.flashback.formula1.model.CircuitSummary?>
    fun getConstructor(season: Int, constructorId: String): Flow<tmg.flashback.formula1.model.Constructor?>
    fun getDriver(season: Int, driver: String): Flow<tmg.flashback.formula1.model.Driver?>
    fun getAllConstructors(season: Int): Flow<List<tmg.flashback.formula1.model.Constructor>>
    fun getSeasonOverview(season: Int): Flow<tmg.flashback.formula1.model.Season>
    fun getSeasonRound(season: Int, round: Int): Flow<tmg.flashback.formula1.model.Round?>
}