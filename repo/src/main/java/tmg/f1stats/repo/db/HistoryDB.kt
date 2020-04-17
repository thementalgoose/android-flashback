package tmg.f1stats.repo.db

import tmg.f1stats.repo.models.History

interface HistoryDB {
    suspend fun allHistory(): List<History>
    suspend fun historyFor(season: Int): History?
}