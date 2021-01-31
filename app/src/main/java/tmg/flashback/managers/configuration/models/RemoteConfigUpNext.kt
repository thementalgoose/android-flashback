package tmg.flashback.managers.configuration.models

import org.threeten.bp.format.DateTimeParseException
import tmg.flashback.core.model.Timestamp
import tmg.flashback.core.model.UpNextSchedule
import tmg.flashback.firebase.base.ConverterUtils

data class RemoteConfigUpNext(
    val schedule: List<RemoteConfigUpNextSchedule>? = null
)

data class RemoteConfigUpNextSchedule(
    val s: Int? = null,
    val r: Int? = null,
    val name: String? = null,
    val date: String? = null,
    val time: String? = null,
    val flag: String? = null,
    val circuit: String? = null,
    val circuitName: String? = null
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
    if (this.s == null || this.name == null || this.date == null) {
        return null
    }

    val date = try {
        ConverterUtils.fromDateRequired(this.date)
    } catch (e: DateTimeParseException) {
        /* Do nothing */
        return null
    }

    return UpNextSchedule(
        season = this.s,
        round = this.r ?: 0,
        name = this.name,
        timestamp = Timestamp(date, ConverterUtils.fromTime(this.time)),
        flag = flag,
        circuitId = circuit,
        circuitName = circuitName
    )
}

//endregion