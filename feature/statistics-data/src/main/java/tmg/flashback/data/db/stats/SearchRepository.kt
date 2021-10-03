package tmg.flashback.data.db.stats

import kotlinx.coroutines.flow.Flow
import tmg.flashback.data.models.stats.History
import tmg.flashback.data.models.stats.SearchCircuit
import tmg.flashback.data.models.stats.SearchConstructor
import tmg.flashback.data.models.stats.SearchDriver

interface SearchRepository {
    fun allDrivers(): Flow<List<SearchDriver>>
    fun allConstructors(): Flow<List<SearchConstructor>>
    fun allCircuits(): Flow<List<SearchCircuit>>
    fun allRaces(): Flow<List<History>>
}