package tmg.flashback.statistics.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.statistics.repo.OverviewRepository

class WeekendOverviewUseCase(
    private val overviewRepository: OverviewRepository,
    private val fetchSeasonUseCase: FetchSeasonUseCase,
) {
    fun get(season: Int): Flow<List<OverviewRace>?> = fetchSeasonUseCase
        .fetch(season)
        .flatMapLatest { hasMadeRequest ->
            overviewRepository
                .getOverview(season)
                .map { overview ->
                    if (!hasMadeRequest) {
                        return@map null
                    }
                    return@map overview.overviewRaces
                }
        }
}