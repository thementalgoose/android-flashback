package tmg.flashback.firebase.converters

import com.google.type.DateTime
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeParseException
import tmg.flashback.firebase.models.FUpNext
import tmg.flashback.firebase.models.FUpNextSchedule
import tmg.flashback.repo.models.remoteconfig.UpNextSchedule

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
         fromDate(this.date)
    } catch (e: DateTimeParseException) {
        /* Do nothing */
        return null
    }

    return UpNextSchedule(
        season = this.s,
        round = this.r ?: 0,
        name = this.name,
        date = date,
        time = fromTime(this.time),
        flag = flag,
        circuitId = circuit,
        circuitName = circuitName
    )
}