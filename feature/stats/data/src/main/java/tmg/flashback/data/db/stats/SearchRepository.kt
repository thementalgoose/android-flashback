package tmg.flashback.data.db.stats

import kotlinx.coroutines.flow.Flow
import tmg.flashback.formula1.model.History
import tmg.flashback.formula1.model.SearchCircuit
import tmg.flashback.formula1.model.SearchConstructor
import tmg.flashback.formula1.model.SearchDriver

interface SearchRepository {
    fun allDrivers(): Flow<List<tmg.flashback.formula1.model.SearchDriver>>
    fun allConstructors(): Flow<List<tmg.flashback.formula1.model.SearchConstructor>>
    fun allCircuits(): Flow<List<tmg.flashback.formula1.model.SearchCircuit>>
    fun allRaces(): Flow<List<tmg.flashback.formula1.model.History>>
}