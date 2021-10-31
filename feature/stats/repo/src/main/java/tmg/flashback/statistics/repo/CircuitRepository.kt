package tmg.flashback.statistics.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tmg.crash_reporting.controllers.CrashController
import tmg.flashback.formula1.model.Circuit
import tmg.flashback.statistics.network.api.FlashbackApi
import tmg.flashback.statistics.network.utils.data
import tmg.flashback.statistics.network.utils.hasData
import tmg.flashback.statistics.repo.mappers.app.CircuitMapper
import tmg.flashback.statistics.repo.mappers.network.NetworkCircuitMapper
import tmg.flashback.statistics.room.FlashbackDatabase
import java.lang.RuntimeException

class CircuitRepository(
    private val api: FlashbackApi,
    private val persistence: FlashbackDatabase,
    private val crashController: CrashController,
    private val networkCircuitMapper: NetworkCircuitMapper,
    private val circuitMapper: CircuitMapper
) {

    suspend fun fetchCircuit(id: String): Boolean {
        val result = api.getCircuit(id)
        if (!result.hasData) {
            return false
        }

        val saveModel = try {
            networkCircuitMapper.mapCircuit(result.data())
        } catch (e: RuntimeException) {
            crashController.logException(e, "fetchCircuit $id")
            null
        }
        if (saveModel != null) {
//            persistence.circuitDao().insertCircuitHistory(saveModel)
            return true
        }
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
        return persistence.circuitDao().observeCircuit(id)
            .map { circuitMapper.mapCircuit(it) }
    }
}