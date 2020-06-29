package tmg.flashback.repo.db.stats

import kotlinx.coroutines.flow.Flow
import tmg.flashback.repo.models.stats.Circuit
import tmg.flashback.repo.models.stats.CircuitSummary

interface CircuitDB {
    fun getCircuit(id: String): Flow<Circuit?>
}