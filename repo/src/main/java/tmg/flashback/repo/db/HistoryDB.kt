package tmg.flashback.repo.db

import tmg.flashback.repo.models.History

interface HistoryDB {
    suspend fun allHistory(): List<History>
    suspend fun historyFor(season: Int): History?
}