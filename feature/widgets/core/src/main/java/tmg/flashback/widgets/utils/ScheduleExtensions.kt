package tmg.flashback.widgets.utils

import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.formula1.model.Schedule
import tmg.utilities.extensions.startOfWeek

// TODO: Move this logic to something testable
internal fun Schedule.labels(): Pair<String, String> {
    val deviceTime = this.timestamp.deviceLocalDateTime

    val sameWeek = LocalDate.now().startOfWeek() == deviceTime.toLocalDate().startOfWeek()
    return if (sameWeek) {
        deviceTime.format(DateTimeFormatter.ofPattern("EEE")) to deviceTime.format(
            DateTimeFormatter.ofPattern(
                "HH:mm"
            )
        )
    } else {
        "${deviceTime.dayOfMonth} ${deviceTime.format(DateTimeFormatter.ofPattern("MMM"))}" to deviceTime.format(
            DateTimeFormatter.ofPattern("HH:mm")
        )
    }
}