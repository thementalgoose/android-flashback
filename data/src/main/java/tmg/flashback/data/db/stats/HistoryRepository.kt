package tmg.flashback.data.db.stats

import kotlinx.coroutines.flow.Flow
import tmg.flashback.data.models.stats.History
import tmg.flashback.data.models.stats.WinnerSeason

interface HistoryRepository {
    fun historyFor(season: Int): Flow<History?>
}