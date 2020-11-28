package tmg.flashback.repo.db.stats

import kotlinx.coroutines.flow.Flow
import tmg.flashback.repo.models.stats.Circuit

interface CircuitDB {
    fun getCircuit(id: String): Flow<Circuit?>
}