package tmg.flashback.domain.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tmg.flashback.crashlytics.manager.CrashlyticsManager
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.domain.persistence.FlashbackDatabase
import tmg.flashback.domain.repo.base.BaseRepository
import tmg.flashback.domain.repo.mappers.app.DriverDataMapper
import tmg.flashback.domain.repo.mappers.app.DriverMapper
import tmg.flashback.domain.repo.mappers.network.NetworkCircuitDataMapper
import tmg.flashback.domain.repo.mappers.network.NetworkConstructorDataMapper
import tmg.flashback.domain.repo.mappers.network.NetworkDriverDataMapper
import tmg.flashback.domain.repo.mappers.network.NetworkDriverMapper
import tmg.flashback.domain.repo.mappers.network.NetworkRaceDataMapper
import tmg.flashback.flashbackapi.api.api.FlashbackApi
import tmg.flashback.formula1.model.DriverHistory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DriverRepository @Inject constructor(
    private val api: FlashbackApi,
    private val persistence: FlashbackDatabase,
    crashController: CrashlyticsManager,
    networkConnectivityManager: NetworkConnectivityManager,
    private val networkDriverMapper: NetworkDriverMapper,
    private val networkDriverDataMapper: NetworkDriverDataMapper,
    private val networkConstructorDataMapper: NetworkConstructorDataMapper,
    private val networkCircuitDataMapper: NetworkCircuitDataMapper,
    private val networkRaceDataMapper: NetworkRaceDataMapper,
    private val driverMapper: DriverMapper,
    private val driverDataMapper: DriverDataMapper,
): BaseRepository(crashController, networkConnectivityManager) {

    /**
     * drivers.json
     * Fetch driver data for all drivers currently available
     */
    suspend fun fetchDrivers(): Boolean = attempt(
        apiCall = api::getDrivers,
        msgIfFailed = "drivers.json"
    ) { data ->
        val allDrivers = data.values.map {
            networkDriverDataMapper.mapDriverData(it)
        }
        persistence.driverDao().insertAll(allDrivers)
        return@attempt true
    }

    /**
     * drivers/{driverId}.json
     * Fetch driver data and history for a specific driver [driverId]
     * @param driverId
     */
    suspend fun fetchDriver(driverId: String): Boolean = attempt(
        apiCall = suspend { api.getDriver(driverId) },
        msgIfFailed = "drivers/${driverId}.json"
    ) { data ->

        saveConstructors(data)
        saveRaceAndCircuits(data)

        val driver = networkDriverDataMapper.mapDriverData(data.driver)
        val allStandings = data.standings.values
            .map { standing ->
                networkDriverMapper.mapDriverSeason(data.driver.id, standing)
            }
        val allStandingRaces = data.standings.values
            .map { standing -> standing.races.values.map { Pair(standing.season, it) } }
            .flatten()
            .map { (season, race) -> networkDriverMapper.mapDriverSeasonRace(data.driver.id, season, race) }

        persistence.driverDao().insertDriver(driver, allStandings, allStandingRaces)

        return@attempt true
    }

    fun getDriverOverview(id: String): Flow<DriverHistory?> {
        return persistence.driverDao().getDriverHistory(id)
            .map { driverMapper.mapDriver(it) }
    }

    fun getDrivers(): Flow<List<tmg.flashback.formula1.model.Driver>> {
        return persistence.driverDao().getDrivers()
            .map { list -> list.map { driverDataMapper.mapDriver(it) } }
    }

    suspend fun getDriverSeasonCount(id: String): Int {
        return persistence.driverDao().getDriverSeasonCount(id)
    }

    private fun saveConstructors(data: tmg.flashback.flashbackapi.api.models.drivers.DriverHistory): Boolean {
        val constructors = data.standings.values
            .map { it.races.values.map { it.construct } }
            .flatten()
            .toSet()
            .map { networkConstructorDataMapper.mapConstructorData(it) }
        persistence.constructorDao().insertAll(constructors)
        return true
    }

    private fun saveRaceAndCircuits(data: tmg.flashback.flashbackapi.api.models.drivers.DriverHistory): Boolean {
        val races = data.standings.values
            .map { it.races.values.map { it.race } }
            .flatten()
            .toSet()
        val circuits = races
            .map { it.circuit }
            .toSet()
        persistence.circuitDao().insertCircuit(circuits.map { networkCircuitDataMapper.mapCircuitData(it) })
        persistence.seasonDao().insertRaceData(races.map { networkRaceDataMapper.mapRaceData(it) })
        return true
    }
}