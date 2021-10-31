package tmg.flashback.di_old.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import tmg.flashback.formula1.model.SeasonOverview

internal object MockHistoryRepository: HistoryRepository {

    override fun historyFor(season: Int): Flow<SeasonOverview?> = flow {
        emit(MOCK_SEASON_OVERVIEW)
    }
}