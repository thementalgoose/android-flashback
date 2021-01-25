package tmg.flashback.controllers

import org.threeten.bp.LocalDate
import tmg.flashback.repo.config.RemoteConfigRepository
import tmg.flashback.repo.models.remoteconfig.UpNextSchedule

/**
 * Up Next functionality on the home screen
 */
class UpNextController(
        private val remoteConfigRepository: RemoteConfigRepository
) {

    /**
     * Get the next race to display in the up next schedule
     *  Up to and including today
     */
    fun getNextEvent(): UpNextSchedule? {
        return remoteConfigRepository
                .upNext
                .filter { schedule ->
                    return@filter schedule.date >= LocalDate.now()
                }
                .minByOrNull { it.date }
    }
}