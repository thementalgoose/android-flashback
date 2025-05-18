package tmg.flashback.data.repo.usecases

import tmg.flashback.data.repo.RaceRepository
import javax.inject.Inject

class HasSyncedSeasonUseCase @Inject constructor(
    private val racesRepository: RaceRepository
) {
    fun hasSynced(season: Int): Boolean =
        racesRepository.hasPreviouslySynced(season)
}