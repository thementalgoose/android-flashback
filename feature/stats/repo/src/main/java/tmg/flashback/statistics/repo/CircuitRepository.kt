package tmg.flashback.statistics.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tmg.crash_reporting.controllers.CrashController
import tmg.flashback.formula1.model.Circuit
import tmg.flashback.formula1.model.CircuitHistory
import tmg.flashback.statistics.network.api.FlashbackApi
import tmg.flashback.statistics.network.utils.data
import tmg.flashback.statistics.network.utils.hasData
import tmg.flashback.statistics.repo.base.BaseRepository
import tmg.flashback.statistics.repo.mappers.app.CircuitMapper
import tmg.flashback.statistics.repo.mappers.network.NetworkCircuitMapper
import tmg.flashback.statistics.room.FlashbackDatabase
import java.lang.RuntimeException

class CircuitRepository(
    private val api: FlashbackApi,
    private val persistence: FlashbackDatabase,
    crashController: CrashController,
    private val networkCircuitMapper: NetworkCircuitMapper,
    private val circuitMapper: CircuitMapper,
): BaseRepository(crashController) {

    suspend fun fetchCircuit(id: String): Boolean = attempt(msgIfFailed = "circuits/${id}.json") {
        val result = api.getCircuit(id)
        if (!result.hasData) { return@attempt false }

        val data = result.data() ?: return@attempt false

        val circuit = networkCircuitMapper.mapCircuitData(data.data)
        val circuitRounds = (data.results?.values ?: emptyList()).map {
            networkCircuitMapper.mapCircuitRounds(data.data.id, it)
        }

        persistence.circuitDao().insertCircuitRounds(circuitRounds)
        persistence.circuitDao().insertCircuit(listOf(circuit))

        return@attempt true
    }

    suspend fun fetchAllCircuits(): Boolean = attempt(msgIfFailed = "circuits.json") {
        val result = api.getCircuits()
        if (!result.hasData) { return@attempt false }

        val data = result.data() ?: return@attempt false

        val allCircuits = data.values
            .map {
                networkCircuitMapper.mapCircuitData(it)
            }

        persistence.circuitDao().insertCircuit(allCircuits)
        return@attempt allCircuits.isNotEmpty()
    }

    fun getCircuit(id: String): Flow<Circuit?> {
        return persistence.circuitDao().getCircuit(id)
            .map { circuitMapper.mapCircuit(it) }
    }

    fun getCircuitHistory(id: String): Flow<CircuitHistory?> {
        return persistence.circuitDao().getCircuitHistory(id)
            .map { model ->
                circuitMapper.mapCircuitHistory(model)
            }
    }
}