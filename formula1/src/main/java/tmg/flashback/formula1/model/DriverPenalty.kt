package tmg.flashback.formula1.model

import org.threeten.bp.LocalDate

@Deprecated("Should not be used anymore")
data class DriverPenalty(
    val season: Int,
    val round: Int? = null,
    val driverWithEmbeddedConstructor: DriverWithEmbeddedConstructor,
    val pointsDelta: Int,
    val date: LocalDate? = null,
    val fine: Int? = null
)