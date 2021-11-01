package tmg.flashback.statistics.repo

import tmg.crash_reporting.controllers.CrashController
import tmg.flashback.statistics.network.api.FlashbackApi
import tmg.flashback.statistics.network.utils.data
import tmg.flashback.statistics.network.utils.hasData
import tmg.flashback.statistics.repo.base.BaseRepository
import tmg.flashback.statistics.repo.mappers.app.ConstructorMapper
import tmg.flashback.statistics.repo.mappers.network.NetworkConstructorMapper
import tmg.flashback.statistics.room.FlashbackDatabase

class ConstructorRepository(
    private val api: FlashbackApi,
    private val persistence: FlashbackDatabase,
    crashController: CrashController,
    private val networkConstructorMapper: NetworkConstructorMapper,
    private val constructorMapper: ConstructorMapper
): BaseRepository(crashController) {

    suspend fun fetchAllConstructors(): Boolean = attempt(msgIfFailed = "constructors.json") {
        val result = api.getConstructors()
        if (!result.hasData) return@attempt false

        val data = result.data()

        val allConstructors = (data?.values ?: emptyList()).map {
            networkConstructorMapper.mapConstructorData(it)
        }

        persistence.constructorDao().insertAll(allConstructors)

        return@attempt true
    }
}