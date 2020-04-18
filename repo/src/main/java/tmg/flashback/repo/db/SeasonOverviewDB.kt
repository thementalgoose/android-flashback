package tmg.flashback.repo.db

import tmg.flashback.repo.models.Circuit
import tmg.flashback.repo.models.Constructor
import tmg.flashback.repo.models.Driver
import tmg.flashback.repo.models.Round

interface SeasonOverviewDB {
    suspend fun getCircuits(season: Int): List<Circuit>
    suspend fun getCircuit(season: Int, round: Int): Circuit?
    suspend fun getConstructor(season: Int, constructorId: String): Constructor?
    suspend fun getDriver(season: Int, driver: String): Driver?
    suspend fun getAllConstructors(season: Int): List<Constructor>
    suspend fun getSeasonOverview(season: Int): List<Round>
    suspend fun getPreviousWeekend(season: Int): Round? // null = first weekend
    suspend fun getNextWeekend(season: Int): Round? // null = last weekend
    suspend fun getSeasonRound(season: Int, round: Int): Round?
}