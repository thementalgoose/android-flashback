package tmg.flashback.data.db.stats

import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun allDrivers(): Flow<List<tmg.flashback.formula1.model.SearchDriver>>
    fun allConstructors(): Flow<List<tmg.flashback.formula1.model.SearchConstructor>>
    fun allCircuits(): Flow<List<tmg.flashback.formula1.model.SearchCircuit>>
    fun allRaces(): Flow<List<tmg.flashback.formula1.model.SeasonOverview>>
}