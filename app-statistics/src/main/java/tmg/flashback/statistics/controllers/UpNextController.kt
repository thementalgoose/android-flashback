package tmg.flashback.statistics.controllers

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.core.controllers.ConfigurationController
import tmg.flashback.core.model.TimeListDisplayType
import tmg.flashback.core.model.UpNextSchedule
import tmg.flashback.core.repositories.CoreRepository
import tmg.flashback.data.repositories.AppRepository

/**
 * Up Next functionality on the home screen
 */
class UpNextController(
    private val coreRepository: CoreRepository,
    private val configurationController: ConfigurationController
) {

    /**
     * The value that the menu should default too when showing time list types
     */
    var upNextDisplayType: TimeListDisplayType
        get() = coreRepository.displayListTypePref
        set(value) {
            coreRepository.displayListTypePref = value
        }

    /**
     * Get the next race to display in the up next schedule
     *  Up to and including today
     */
    // item 0 :yesterday
    // item 1 :today, today2
    // item 2: tomorrow
    fun getNextEvent(): UpNextSchedule? {

        return configurationController
            .upNext
            .filter { schedule ->
                schedule.values.any { it.timestamp.originalDate >= LocalDate.now() }
            }
            .filter { schedule ->
                schedule.values.minByOrNull { it.timestamp.originalDate.atTime(it.timestamp.originalTime ?: LocalTime.parse("10:00", DateTimeFormatter.ofPattern("HH:mm"))) } != null
            }
            .minByOrNull { schedule ->
                val minDateInEvent = schedule.values.minByOrNull { it.timestamp.originalDate.atTime(it.timestamp.originalTime ?: LocalTime.parse("10:00", DateTimeFormatter.ofPattern("HH:mm"))) }!!
                return@minByOrNull minDateInEvent.timestamp.string()
            }
    }
}