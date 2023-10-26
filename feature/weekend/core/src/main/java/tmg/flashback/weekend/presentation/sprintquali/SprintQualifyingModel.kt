package tmg.flashback.weekend.presentation.sprintquali

import tmg.flashback.formula1.model.DriverEntry
import tmg.flashback.formula1.model.SprintQualifyingResult


sealed class SprintQualifyingModel(
    val id: String,
    val isResult: Boolean
) {

    object NotAvailableYet: SprintQualifyingModel(
        id = "not_available_yet",
        isResult = false
    )

    object NotAvailable: SprintQualifyingModel(
        id = "not_available",
        isResult = false
    )

    object Loading: SprintQualifyingModel(
        id = "loading",
        isResult = false
    )

    data class Result(
        val driver: DriverEntry,
        private val finalQualifyingPosition: Int?,
        val sq1: SprintQualifyingResult?,
        val sq2: SprintQualifyingResult?,
        val sq3: SprintQualifyingResult?,
        val qualified: Int? = finalQualifyingPosition ?: sq3?.position ?: sq2?.position ?: sq1?.position,
        val grid: Int?
    ) : SprintQualifyingModel(driver.driver.id, true) {
        companion object
    }
}