package tmg.flashback.di.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import tmg.flashback.data.db.stats.HistoryRepository
import tmg.flashback.data.models.stats.History
import tmg.flashback.data.models.stats.WinnerSeason

internal object MockHistoryRepository: HistoryRepository {

    override fun historyFor(season: Int): Flow<History?> = flow {
        emit(mockHistory)
    }
}