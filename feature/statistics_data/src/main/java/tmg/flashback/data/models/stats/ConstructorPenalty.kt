package tmg.flashback.data.models.stats

import org.threeten.bp.LocalDate

data class ConstructorPenalty(
        val season: Int,
        val round: Int? = null,
        val constructor: Constructor,
        val pointsDelta: Int,
        val date: LocalDate? = null,
        val fine: Int? = null
)