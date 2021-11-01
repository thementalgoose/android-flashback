package tmg.flashback.statistics.repo

import tmg.crash_reporting.controllers.CrashController
import tmg.flashback.statistics.network.api.FlashbackApi
import tmg.flashback.statistics.network.utils.data
import tmg.flashback.statistics.network.utils.hasData
import tmg.flashback.statistics.repo.base.BaseRepository
import tmg.flashback.statistics.repo.mappers.app.CircuitMapper
import tmg.flashback.statistics.repo.mappers.app.DriverMapper
import tmg.flashback.statistics.repo.mappers.network.NetworkCircuitMapper
import tmg.flashback.statistics.repo.mappers.network.NetworkDriverMapper
import tmg.flashback.statistics.room.FlashbackDatabase

class DriverRepository(
    private val api: FlashbackApi,
    private val persistence: FlashbackDatabase,
    crashController: CrashController,
    private val networkDriverMapper: NetworkDriverMapper,
    private val driverMapper: DriverMapper
): BaseRepository(crashController) {

    suspend fun fetchAllDrivers(): Boolean = attempt(msgIfFailed = "drivers.json") {
        val result = api.getDrivers()
        if (!result.hasData) return@attempt false

        val data = result.data()

        val allDrivers = (data?.values ?: emptyList()).map {
            networkDriverMapper.mapDriverData(it)
        }

        persistence.driverDao().insertAll(allDrivers)

        return@attempt true
    }
}