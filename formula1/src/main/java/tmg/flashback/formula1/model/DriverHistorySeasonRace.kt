package tmg.flashback.formula1.model

import org.threeten.bp.LocalDate

data class DriverHistorySeasonRace(
    val status: String,
    val finished: Int,
    val points: Double,
    val qualified: Int?,
    val gridPos: Int?,
    val round: Int,
    val season: Int,
    val raceName: String,
    val date: LocalDate,
    val constructor: Constructor?,
    val circuitName: String,
    val circuitId: String,
    val circuitNationality: String,
    val circuitNationalityISO: String
)