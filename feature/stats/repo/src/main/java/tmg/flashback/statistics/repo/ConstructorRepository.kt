package tmg.flashback.statistics.repo

import tmg.crash_reporting.controllers.CrashController
import tmg.flashback.statistics.network.api.FlashbackApi
import tmg.flashback.statistics.repo.base.BaseRepository
import tmg.flashback.statistics.repo.mappers.app.ConstructorMapper
import tmg.flashback.statistics.repo.mappers.network.NetworkConstructorDataMapper
import tmg.flashback.statistics.room.FlashbackDatabase

class ConstructorRepository(
    private val api: FlashbackApi,
    private val persistence: FlashbackDatabase,
    crashController: CrashController,
    private val networkConstructorDataMapper: NetworkConstructorDataMapper,
    private val constructorMapper: ConstructorMapper
): BaseRepository(crashController) {

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

        return@attempt true
    }
}