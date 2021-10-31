package tmg.flashback.formula1.model

import org.threeten.bp.LocalDate

data class DriverPenalty(
    val season: Int,
    val round: Int? = null,
    val driver: Driver,
    val pointsDelta: Int,
    val date: LocalDate? = null,
    val fine: Int? = null
)