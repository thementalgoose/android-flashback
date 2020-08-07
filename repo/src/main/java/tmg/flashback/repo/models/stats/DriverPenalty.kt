package tmg.flashback.repo.models.stats

import org.threeten.bp.LocalDate

data class DriverPenalty(
        val season: Int,
        val driver: Driver,
        val pointsDelta: Int,
        val date: LocalDate,
        val fine: Int
)