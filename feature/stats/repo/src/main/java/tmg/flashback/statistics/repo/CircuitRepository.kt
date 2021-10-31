package tmg.flashback.statistics.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tmg.crash_reporting.controllers.CrashController
import tmg.flashback.formula1.model.Circuit
import tmg.flashback.formula1.model.CircuitHistory
import tmg.flashback.statistics.network.api.FlashbackApi
import tmg.flashback.statistics.network.utils.data
import tmg.flashback.statistics.network.utils.hasData
import tmg.flashback.statistics.repo.mappers.app.CircuitMapper
import tmg.flashback.statistics.repo.mappers.network.NetworkCircuitMapper
import tmg.flashback.statistics.room.FlashbackDatabase
import tmg.flashback.statistics.room.dao.DriverDao
import java.lang.Exception
import java.lang.RuntimeException

class CircuitRepository(
    private val api: FlashbackApi,
    private val persistence: FlashbackDatabase,
    private val crashController: CrashController,
    private val networkCircuitMapper: NetworkCircuitMapper,
    private val circuitMapper: CircuitMapper,
) {

    suspend fun fetchCircuit(id: String): Boolean {
        val result = api.getCircuit(id)
        if (!result.hasData) {
            return false
        }

        val data = result.data() ?: return false

        val circuit = attempt("fetchCircuit $id data") {
            networkCircuitMapper.mapCircuitData(data.data)
        } ?: return false
        val circuitRounds = attempt(msgIfFailed = "fetchCircuit $id rounds") {
            (data.results?.values ?: emptyList()).map {
                networkCircuitMapper.mapCircuitRounds(data.data.id, it)
            }
        } ?: return false

        persistence.circuitDao().insertCircuitRounds(circuitRounds)
        persistence.circuitDao().insertCircuit(listOf(circuit))

        return false
    }

    suspend fun fetchAllCircuits(): Boolean {
        val result = api.getCircuits()
        if (!result.hasData) {
            return false
        }
        val allCircuits = (result.data()?.values ?: emptyList())
            .mapNotNull {
                try {
                    networkCircuitMapper.mapCircuitData(it)
                } catch (e: RuntimeException) {
                    crashController.logException(e, "fetchAllCircuits parsing ${it.id}")
                    null
                }
            }
        persistence.circuitDao().insertCircuit(allCircuits)
        return allCircuits.isNotEmpty()
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

    fun <T> attempt(msgIfFailed: String? = null, closure: () -> T?): T? {
        return try {
            closure.invoke()
        } catch (e: RuntimeException) {
            crashController.logException(e, msgIfFailed)
            null
        }
    }
}