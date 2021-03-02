package tmg.flashback.statistics.controllers

import org.threeten.bp.LocalDate
import tmg.flashback.core.controllers.ConfigurationController
import tmg.flashback.core.model.UpNextSchedule

/**
 * Up Next functionality on the home screen
 */
class UpNextController(
        private val configurationController: ConfigurationController
) {

    /**
     * Get the next race to display in the up next schedule
     *  Up to and including today
     */
    fun getNextEvent(): UpNextSchedule? {
        return configurationController
                .upNext
                .filter { schedule ->
                    return@filter schedule.timestamp.deviceDate >= LocalDate.now()
                }
                .minByOrNull { it.timestamp.deviceDate }
    }
}