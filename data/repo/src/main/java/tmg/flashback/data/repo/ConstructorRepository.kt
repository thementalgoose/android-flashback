package tmg.flashback.data.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tmg.flashback.crashlytics.manager.CrashlyticsManager
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.data.persistence.FlashbackDatabase
import tmg.flashback.data.repo.base.BaseRepository
import tmg.flashback.data.repo.mappers.app.ConstructorDataMapper
import tmg.flashback.data.repo.mappers.app.ConstructorMapper
import tmg.flashback.data.repo.mappers.network.NetworkConstructorDataMapper
import tmg.flashback.data.repo.mappers.network.NetworkConstructorMapper
import tmg.flashback.data.repo.mappers.network.NetworkDriverDataMapper
import tmg.flashback.flashbackapi.api.api.FlashbackApi
import tmg.flashback.formula1.model.ConstructorHistory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConstructorRepository @Inject constructor(
    private val api: FlashbackApi,
    private val persistence: FlashbackDatabase,
    crashController: CrashlyticsManager,
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

        val constructor = networkConstructorDataMapper.mapConstructorData(data.constructor)
        val allSeasons = data.standings.values
            .map { standing ->
                networkConstructorMapper.mapConstructorSeason(data.constructor.id, standing)
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

    private fun saveDrivers(data: tmg.flashback.flashbackapi.api.models.constructors.ConstructorHistory): Boolean {
        val drivers = data.standings.values
            .map { it.drivers.values.map { it.driver } }
            .flatten()
            .toSet()
            .map { networkDriverDataMapper.mapDriverData(it) }
        persistence.driverDao().insertAll(drivers)
        return true
    }
}