package tmg.f1stats.repo.db

import tmg.f1stats.repo.models.Circuit
import tmg.f1stats.repo.models.Constructor
import tmg.f1stats.repo.models.SeasonRound

interface SeasonOverviewDB {
    fun getCircuit(season: Int): Circuit
    fun getAllConstructors(season: Int): List<Constructor>
    fun getSeasonOverview(season: Int): List<SeasonRound>
    fun getLastWeekend(season: Int): SeasonRound? // null = first weekend
    fun getNextWeekend(season: Int): SeasonRound? // null = last weekend
    fun getWeekend(season: Int, round: Int)
}