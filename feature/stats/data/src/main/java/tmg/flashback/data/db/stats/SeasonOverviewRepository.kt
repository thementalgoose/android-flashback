package tmg.flashback.data.db.stats

import kotlinx.coroutines.flow.Flow
import tmg.flashback.formula1.model.Circuit

interface SeasonOverviewRepository {
    fun getCircuits(season: Int): Flow<List<Circuit>>
    fun getCircuit(season: Int, round: Int): Flow<Circuit?>
    fun getConstructor(season: Int, constructorId: String): Flow<tmg.flashback.formula1.model.Constructor?>
    fun getDriver(season: Int, driver: String): Flow<tmg.flashback.formula1.model.DriverWithEmbeddedConstructor?>
    fun getAllConstructors(season: Int): Flow<List<tmg.flashback.formula1.model.Constructor>>
    fun getSeasonOverview(season: Int): Flow<tmg.flashback.formula1.model.Season>
    fun getSeasonRound(season: Int, round: Int): Flow<tmg.flashback.formula1.model.Round?>
}