package tmg.flashback.data.repo.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import tmg.flashback.data.repo.EventsRepository
import tmg.flashback.data.repo.OverviewRepository
import tmg.flashback.data.repo.RaceRepository
import javax.inject.Inject

class FetchSeasonUseCase @Inject constructor(
    private val overviewRepository: OverviewRepository,
    private val eventsRepository: EventsRepository,
    private val raceRepository: RaceRepository,
) {
    fun fetch(season: Int): Flow<Boolean> = flow {
        if (!raceRepository.hasPreviouslySynced(season)) {
            emit(false)
            fetchSeason(season)
            emit(true)
        } else {
            emit(true)
        }
    }

    // TODO: See DashboardSyncUseCase to optimise this!
    suspend fun fetchSeason(season: Int): Boolean {
        overviewRepository.fetchOverview(season)
        raceRepository.fetchRaces(season)
        eventsRepository.fetchEvents(season)
        return true
    }
}