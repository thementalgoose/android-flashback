package tmg.flashback.data.db.stats

import kotlinx.coroutines.flow.Flow
import tmg.flashback.data.models.stats.SearchCircuit
import tmg.flashback.data.models.stats.SearchConstructor
import tmg.flashback.data.models.stats.SearchDriver
import tmg.flashback.data.models.stats.SearchRace

interface SearchRepository {
    fun allDrivers(): Flow<SearchDriver>
    fun allConstructors(): Flow<SearchConstructor>
    fun allCircuits(): Flow<SearchCircuit>
    fun allRaces(): Flow<SearchRace>
}