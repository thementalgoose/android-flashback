package tmg.flashback.statistics.repo

import tmg.crash_reporting.controllers.CrashController
import tmg.flashback.statistics.network.api.FlashbackApi
import tmg.flashback.statistics.repo.mappers.app.CircuitMapper
import tmg.flashback.statistics.repo.mappers.network.NetworkCircuitMapper
import tmg.flashback.statistics.room.FlashbackDatabase

class DriverRepository(
    private val api: FlashbackApi,
    private val persistence: FlashbackDatabase,
    private val crashController: CrashController,
    private val networkCircuitMapper: NetworkCircuitMapper,
    private val circuitMapper: CircuitMapper
) {

}