package tmg.flashback.managers.configuration.models

import org.threeten.bp.format.DateTimeParseException
import tmg.flashback.core.model.Timestamp
import tmg.flashback.core.model.UpNextSchedule
import tmg.flashback.core.model.UpNextScheduleTimestamp
import tmg.flashback.firebase.base.ConverterUtils

data class RemoteConfigUpNext(
    val schedule: List<RemoteConfigUpNextSchedule>? = null
)

data class RemoteConfigUpNextSchedule(
    val s: Int? = null,
    val r: Int? = null,
    val title: String? = null,
    val subtitle: String? = null,
    val dates: List<RemoteConfigUpNextItem>? = null,
    val flag: String? = null,
    val circuit: String? = null,
)

data class RemoteConfigUpNextItem(
    val type: String?,
    val d: String?,
    val t: String?
)

//region Converters

fun RemoteConfigUpNext.convert(): List<UpNextSchedule> {
    if (schedule == null) {
        return emptyList()
    }
    return schedule
        .mapNotNull { it.convert() }
}

fun RemoteConfigUpNextSchedule.convert(): UpNextSchedule? {
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
            ConverterUtils.fromDateRequired(it.d)
        } catch (e: DateTimeParseException) {
            /* Do nothing */
            return@mapNotNull null
        }
        return@mapNotNull UpNextScheduleTimestamp(
            label = it.type,
            timestamp = Timestamp(date, ConverterUtils.fromTime(it.t))
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