package tmg.flashback.data.db.stats

import kotlinx.coroutines.flow.Flow
import tmg.flashback.formula1.model.CircuitHistory

interface CircuitRepository {
    fun getCircuit(id: String): Flow<CircuitHistory?>
}