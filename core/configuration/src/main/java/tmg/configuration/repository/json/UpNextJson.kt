package tmg.configuration.repository.json

import org.threeten.bp.format.DateTimeParseException
import tmg.configuration.repository.models.Timestamp
import tmg.configuration.repository.models.UpNextSchedule
import tmg.configuration.repository.models.UpNextScheduleTimestamp
import tmg.configuration.utils.DateConverters
import tmg.configuration.utils.TimeConverters

data class UpNextJson(
    val schedule: List<UpNextScheduleJson>? = null
)

data class UpNextScheduleJson(
    val s: Int? = null,
    val r: Int? = null,
    val title: String? = null,
    val subtitle: String? = null,
    val dates: List<UpNextItemJson>? = null,
    val flag: String? = null,
    val circuit: String? = null,
)

data class UpNextItemJson(
    val type: String?,
    val d: String?,
    val t: String?
)

//region Converters

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

//endregion