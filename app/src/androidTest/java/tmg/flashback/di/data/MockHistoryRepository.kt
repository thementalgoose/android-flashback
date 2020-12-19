package tmg.flashback.di.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import tmg.flashback.repo.db.stats.HistoryRepository
import tmg.flashback.repo.models.stats.History
import tmg.flashback.repo.models.stats.WinnerSeason

internal object MockHistoryRepository: HistoryRepository {

    override fun allHistory(): Flow<List<History>> = flow { emit(emptyList()) }

    override fun historyFor(season: Int): Flow<History?> = flow {
        emit(mockHistory)
    }

    override fun allWinners(): Flow<List<WinnerSeason>> = flow { emit(emptyList()) }

    override fun winnersFor(season: Int): Flow<WinnerSeason?> = flow { emit(null) }
}