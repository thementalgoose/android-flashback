package tmg.flashback.statistics.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tmg.flashback.crash_reporting.controllers.CrashController
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.formula1.model.ConstructorHistory
import tmg.flashback.statistics.network.api.FlashbackApi
import tmg.flashback.statistics.repo.base.BaseRepository
import tmg.flashback.statistics.repo.mappers.app.ConstructorDataMapper
import tmg.flashback.statistics.repo.mappers.app.ConstructorMapper
import tmg.flashback.statistics.repo.mappers.network.NetworkConstructorDataMapper
import tmg.flashback.statistics.repo.mappers.network.NetworkConstructorMapper
import tmg.flashback.statistics.repo.mappers.network.NetworkDriverDataMapper
import tmg.flashback.statistics.room.FlashbackDatabase

class ConstructorRepository(
    private val api: FlashbackApi,
    private val persistence: FlashbackDatabase,
    crashController: CrashController,
    networkConnectivityManager: NetworkConnectivityManager,
    private val networkDriverDataMapper: NetworkDriverDataMapper,
    private val networkConstructorDataMapper: NetworkConstructorDataMapper,
    private val networkConstructorMapper: NetworkConstructorMapper,
    private val constructorMapper: ConstructorMapper,
    private val constructorDataMapper: ConstructorDataMapper,
): BaseRepository(crashController, networkConnectivityManager) {

    /**
     * constructors.json
     * Fetch constructor data for all constructors currently available
     */
    suspend fun fetchConstructors(): Boolean = attempt(
        apiCall = api::getConstructors,
        msgIfFailed = "constructors.json"
    ) { data ->
        val allConstructors = data.values.map {
            networkConstructorDataMapper.mapConstructorData(it)
        }
        persistence.constructorDao().insertAll(allConstructors)
        return@attempt true
    }

    /**
     * constructors/{constructorId}.json
     * Fetch constructor data and history for a specific constructor [constructorId]
     * @param constructorId
     */
    suspend fun fetchConstructor(constructorId: String): Boolean = attempt(
        apiCall = suspend { api.getConstructor(constructorId) },
        msgIfFailed = "constructors/${constructorId}.json"
    ) { data ->

        saveDrivers(data)

        val constructor = networkConstructorDataMapper.mapConstructorData(data.construct)
        val allSeasons = data.standings.values
            .map { standing ->
                networkConstructorMapper.mapConstructorSeason(data.construct.id, standing)
            }
        val allSeasonDrivers = data.standings.values
            .map { standing -> standing.drivers.values.map { Pair(standing.season, it) } }
            .flatten()
            .map { (season, driver) -> networkConstructorMapper.mapConstructorSeasonDriver(constructor.id, season, driver) }

        persistence.constructorDao().insertConstructor(constructor, allSeasons, allSeasonDrivers)

        return@attempt true
    }

    fun getConstructorOverview(id: String): Flow<ConstructorHistory?> {
        return persistence.constructorDao().getConstructorHistory(id)
            .map { constructorMapper.mapConstructor(it) }
    }

    fun getConstructors(): Flow<List<tmg.flashback.formula1.model.Constructor>> {
        return persistence.constructorDao().getConstructors()
            .map { list -> list.map { constructorDataMapper.mapConstructorData(it) } }
    }

    suspend fun getConstructorSeasonCount(id: String): Int {
        return persistence.constructorDao().getConstructorSeasonCount(id)
    }

    private fun saveDrivers(data: tmg.flashback.statistics.network.models.constructors.ConstructorHistory): Boolean {
        val drivers = data.standings.values
            .map { it.drivers.values.map { it.driver } }
            .flatten()
            .toSet()
            .map { networkDriverDataMapper.mapDriverData(it) }
        persistence.driverDao().insertAll(drivers)
        return true
    }
}