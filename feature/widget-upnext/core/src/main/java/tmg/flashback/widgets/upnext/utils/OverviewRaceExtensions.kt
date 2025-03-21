package tmg.flashback.widgets.upnext.utils

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.formula1.model.Schedule
import tmg.flashback.formula1.model.Timestamp
import tmg.utilities.extensions.startOfWeek

// TODO: Move this logic to something testable
internal fun OverviewRace.raceSchedule(): Schedule? {
    return this.schedule.firstOrNull { it.label.lowercase() == "race" }
}

// TODO: Move this logic to something testable
internal fun OverviewRace.labels(): Pair<String, String> {
    val deviceTime = Timestamp(this.date, this.time ?: LocalTime.of(12, 0)).deviceLocalDateTime

    val sameWeek = LocalDate.now().startOfWeek() == deviceTime.toLocalDate().startOfWeek()
    val timeString = this.time?.let { deviceTime.format(DateTimeFormatter.ofPattern("HH:mm")) } ?: ""
    return if (sameWeek) {
        deviceTime.format(DateTimeFormatter.ofPattern("EEE")) to timeString
    } else {
        "${deviceTime.dayOfMonth} ${deviceTime.format(DateTimeFormatter.ofPattern("MMM"))}" to timeString
    }
}
