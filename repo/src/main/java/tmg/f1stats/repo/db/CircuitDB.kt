package tmg.f1stats.repo.db

import tmg.f1stats.repo.models.Circuit

interface CircuitDB {
    suspend fun getCircuit(circuitId: String): Circuit?
}