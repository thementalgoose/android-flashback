package tmg.flashback.repo.models.stats

import org.threeten.bp.LocalDate

data class ConstructorPenalty(
        val season: Int,
        val constructor: Constructor,
        val pointsDelta: Int,
        val date: LocalDate,
        val fine: Int
)