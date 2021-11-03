package tmg.flashback.data.db.stats

import kotlinx.coroutines.flow.Flow
import tmg.flashback.formula1.model.Circuit
import tmg.flashback.formula1.model.Constructor

interface SeasonOverviewRepository {
    @Deprecated("")
    fun getCircuits(season: Int): Flow<List<Circuit>>
    @Deprecated("")
    fun getCircuit(season: Int, round: Int): Flow<Circuit?>
    @Deprecated("")
    fun getConstructor(season: Int, constructorId: String): Flow<Constructor?>
    @Deprecated("")
    fun getDriver(season: Int, driver: String): Flow<tmg.flashback.formula1.model.DriverWithEmbeddedConstructor?>
    @Deprecated("")
    fun getAllConstructors(season: Int): Flow<List<Constructor>>
    fun getSeasonOverview(season: Int): Flow<tmg.flashback.formula1.model.Season>
    fun getSeasonRound(season: Int, round: Int): Flow<tmg.flashback.formula1.model.Race?>
}