package tmg.flashback.domain.repo.usecases

import tmg.flashback.domain.repo.RaceRepository
import javax.inject.Inject

class HasSyncedSeasonUseCase @Inject constructor(
    private val racesRepository: RaceRepository
) {
    fun hasSynced(season: Int): Boolean =
        !racesRepository.hasntPreviouslySynced(season)
}