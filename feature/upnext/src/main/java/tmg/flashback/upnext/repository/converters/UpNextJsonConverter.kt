package tmg.flashback.upnext.repository.converters

import org.threeten.bp.format.DateTimeParseException
import tmg.configuration.utils.DateConverters
import tmg.configuration.utils.TimeConverters
import tmg.flashback.formula1.model.Timestamp
import tmg.flashback.upnext.repository.json.UpNextJson
import tmg.flashback.upnext.repository.json.UpNextScheduleJson
import tmg.flashback.upnext.repository.model.UpNextSchedule
import tmg.flashback.upnext.repository.model.UpNextScheduleTimestamp

fun UpNextJson.convert(): List<UpNextSchedule> {
    if (schedule == null) {
        return emptyList()
    }
    return schedule
            .mapNotNull { it.convert() }
}

fun UpNextScheduleJson.convert(): UpNextSchedule? {
    if (this.s == null || this.title == null) {
        return null
    }
    if (this.dates == null || this.dates.isEmpty()) {
        return null
    }
    val values = this.dates.mapNotNull {
        if (it.type == null) {
            return@mapNotNull null
        }
        if (it.d == null) {
            return@mapNotNull null
        }
        val date = try {
            DateConverters.fromDateRequired(it.d)
        } catch (e: DateTimeParseException) {
            /* Do nothing */
            return@mapNotNull null
        }
        return@mapNotNull UpNextScheduleTimestamp(
                label = it.type,
                timestamp = Timestamp(date, TimeConverters.fromTime(it.t))
        )
    }
    if (values.isEmpty()) {
        return null
    }

    return UpNextSchedule(
            season = this.s,
            round = this.r ?: 0,
            title = this.title,
            subtitle = this.subtitle,
            values = values,
            flag = flag,
            circuitId = circuit,
    )
}