package tmg.flashback.widgets.upnext.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
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