package tmg.flashback.firebase.converters

import org.threeten.bp.format.DateTimeParseException
import tmg.flashback.firebase.base.ConverterUtils.fromDate
import tmg.flashback.firebase.base.ConverterUtils.fromDateRequired
import tmg.flashback.firebase.base.ConverterUtils.fromTime
import tmg.flashback.firebase.models.FUpNext
import tmg.flashback.firebase.models.FUpNextSchedule
import tmg.flashback.data.models.Timestamp
import tmg.flashback.data.models.remoteconfig.UpNextSchedule

fun FUpNext.convert(): List<UpNextSchedule> {
    if (schedule == null) {
        return emptyList()
    }
    return schedule
        .mapNotNull { it.convert() }
}

fun FUpNextSchedule.convert(): UpNextSchedule? {
    if (this.s == null || this.name == null || this.date == null) {
        return null
    }

    val date = try {
         fromDateRequired(this.date)
    } catch (e: DateTimeParseException) {
        /* Do nothing */
        return null
    }

    return UpNextSchedule(
        season = this.s,
        round = this.r ?: 0,
        name = this.name,
        timestamp = Timestamp(date, fromTime(this.time)),
        flag = flag,
        circuitId = circuit,
        circuitName = circuitName
    )
}