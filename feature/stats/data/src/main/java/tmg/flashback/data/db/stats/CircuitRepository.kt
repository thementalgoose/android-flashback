package tmg.flashback.data.db.stats

import kotlinx.coroutines.flow.Flow
import tmg.flashback.formula1.model.Circuit

interface CircuitRepository {
    fun getCircuit(id: String): Flow<tmg.flashback.formula1.model.Circuit?>
}