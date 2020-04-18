package tmg.flashback.repo.db

import tmg.flashback.repo.models.Circuit

interface CircuitDB {
    suspend fun getCircuit(circuitId: String): Circuit?
}