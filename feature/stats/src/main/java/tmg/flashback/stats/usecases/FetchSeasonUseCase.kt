package tmg.flashback.stats.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import tmg.flashback.statistics.repo.EventsRepository
import tmg.flashback.statistics.repo.OverviewRepository
import tmg.flashback.statistics.repo.RaceRepository

class FetchSeasonUseCase(
    private val overviewRepository: OverviewRepository,
    private val eventsRepository: EventsRepository,
    private val raceRepository: RaceRepository,
) {
    fun fetch(season: Int): Flow<Boolean> = flow {
        if (raceRepository.hasntPreviouslySynced(season)) {
            emit(false)
            fetchSeason(season)
            emit(true)
        } else {
            emit(true)
        }
    }

    suspend fun fetchSeason(season: Int): Boolean {
        overviewRepository.fetchOverview(season)
        raceRepository.fetchRaces(season)
        eventsRepository.fetchEvents(season)
        return true
    }
}