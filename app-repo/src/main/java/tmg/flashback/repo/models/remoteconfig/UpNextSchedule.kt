package tmg.flashback.repo.models.remoteconfig

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime

data class UpNextSchedule(
    val round: Int,
    val name: String,
    val date: LocalDate,
    val time: LocalTime?,
    val flag: String?,
    val circuitId: String?
)