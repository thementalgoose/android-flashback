package tmg.flashback.repo.db.stats

import kotlinx.coroutines.flow.Flow
import tmg.flashback.repo.models.stats.History
import tmg.flashback.repo.models.stats.WinnerSeason

interface HistoryRepository {
    fun historyFor(season: Int): Flow<History?>
}