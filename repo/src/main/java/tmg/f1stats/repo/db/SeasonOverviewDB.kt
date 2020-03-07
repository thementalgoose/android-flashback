package tmg.f1stats.repo.db

import tmg.f1stats.repo.models.Circuit
import tmg.f1stats.repo.models.Constructor
import tmg.f1stats.repo.models.Weekend

interface SeasonOverviewDB {
    fun getCircuit(season: Int): Circuit
    fun getAllConstructors(season: Int): List<Constructor>
    fun getSeasonOverview(season: Int): List<Weekend>
    fun getLastWeekend(season: Int): Weekend? // null = first weekend
    fun getNextWeekend(season: Int): Weekend? // null = last weekend
    fun getWeekend(season: Int, round: Int)
}