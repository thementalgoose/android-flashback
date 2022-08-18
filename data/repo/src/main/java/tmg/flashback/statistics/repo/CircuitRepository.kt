package tmg.flashback.statistics.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tmg.flashback.crash_reporting.controllers.CrashController
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.formula1.model.Circuit
import tmg.flashback.formula1.model.CircuitHistory
import tmg.flashback.statistics.network.api.FlashbackApi
import tmg.flashback.statistics.repo.base.BaseRepository
import tmg.flashback.statistics.repo.mappers.app.CircuitMapper
import tmg.flashback.statistics.repo.mappers.network.*
import tmg.flashback.statistics.room.FlashbackDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CircuitRepository @Inject constructor(
    private val api: FlashbackApi,
    private val persistence: FlashbackDatabase,
    crashController: CrashController,
    networkConnectivityManager: NetworkConnectivityManager,
    private val networkCircuitMapper: NetworkCircuitMapper,
    private val networkDriverDataMapper: NetworkDriverDataMapper,
    private val networkConstructorDataMapper: NetworkConstructorDataMapper,
    private val networkCircuitDataMapper: NetworkCircuitDataMapper,
    private val circuitMapper: CircuitMapper,
): BaseRepository(crashController, networkConnectivityManager) {

    /**
     * circuits/{circuitId}.json
     * Fetch circuit data and history for a specific circuit [circuitId]
     * @param circuitId
     */
    suspend fun fetchCircuit(circuitId: String): Boolean = attempt(
        apiCall = suspend { api.getCircuit(circuitId) },
        msgIfFailed = "circuits/${circuitId}.json"
    ) { data ->
        val circuit = networkCircuitDataMapper.mapCircuitData(data.data)
        val circuitRounds = (data.results?.values ?: emptyList()).map {
            networkCircuitMapper.mapCircuitRounds(data.data.id, it)
        }

        // Drivers
        val drivers = (data.results?.values ?: emptyList())
            .map {
                it.preview?.map { it.driver } ?: emptyList()
            }
            .flatten()
            .distinctBy { it.id }
            .map { networkDriverDataMapper.mapDriverData(it) }
        persistence.driverDao().insertAll(drivers)

        // Constructors
        val constructors = (data.results?.values ?: emptyList())
            .map {
                it.preview?.map { it.constructor } ?: emptyList()
            }
            .flatten()
            .distinctBy { it.id }
            .map { networkConstructorDataMapper.mapConstructorData(it) }
        persistence.constructorDao().insertAll(constructors)

        // Circuit results
        val results = (data.results?.values ?: emptyList())
            .map {
                networkCircuitMapper.mapCircuitPreviews(it)
            }
            .flatten()

        persistence.circuitDao().insertCircuitRounds(circuitRounds)
        persistence.circuitDao().insertCircuit(listOf(circuit))
        persistence.circuitDao().insertCircuitRoundResults(results)

        return@attempt true
    }

    /**
     * circuits.json
     * Fetch circuits data for all circuits currently available
     */
    suspend fun fetchCircuits(): Boolean = attempt(
        apiCall = api::getCircuits,
        msgIfFailed = "circuits.json"
    ) { data ->
        val allCircuits = data.values
            .map { networkCircuitDataMapper.mapCircuitData(it) }

        persistence.circuitDao().insertCircuit(allCircuits)
        return@attempt allCircuits.isNotEmpty()
    }

    fun getCircuits(): Flow<List<Circuit>> {
        return persistence.circuitDao().getCircuits()
            .map { list -> list.mapNotNull { circuitMapper.mapCircuit(it) } }
    }

    fun getCircuit(id: String): Flow<Circuit?> {
        return persistence.circuitDao().getCircuit(id)
            .map { circuitMapper.mapCircuit(it) }
    }

    suspend fun getCircuitRounds(id: String): Int {
        return persistence.circuitDao().getCircuitRounds(id)
    }

    fun getCircuitHistory(id: String): Flow<CircuitHistory?> {
        return persistence.circuitDao().getCircuitHistory(id)
            .map { model ->
                circuitMapper.mapCircuitHistory(model)
            }
    }
}