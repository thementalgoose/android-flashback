package tmg.flashback.upnext.controllers

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import tmg.configuration.controllers.ConfigController
import tmg.configuration.repository.models.TimeListDisplayType
import tmg.configuration.repository.models.UpNextSchedule

/**
 * Up Next functionality on the home screen
 */
class UpNextController(
    private val configurationController: ConfigController
) {

    /**
     * The value that the menu should default too when showing time list types
     */
    var upNextDisplayType: TimeListDisplayType = TimeListDisplayType.RELATIVE
//            coreRepository.displayListTypePref = value

    /**
     * Get the next race to display in the up next schedule
     *  Up to and including today
     */
    // item 0 :yesterday
    // item 1 :today, today2
    // item 2: tomorrow
    // TODO: Move this to a repository layer
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