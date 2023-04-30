package tmg.flashback.results.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import tmg.flashback.domain.repo.EventsRepository
import tmg.flashback.domain.repo.OverviewRepository
import tmg.flashback.domain.repo.RaceRepository
import javax.inject.Inject

class FetchSeasonUseCase @Inject constructor(
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