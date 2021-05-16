package tmg.flashback.data.db.stats

import kotlinx.coroutines.flow.Flow
import tmg.flashback.data.models.stats.Circuit

interface CircuitRepository {
    fun getCircuit(id: String): Flow<Circuit?>
}