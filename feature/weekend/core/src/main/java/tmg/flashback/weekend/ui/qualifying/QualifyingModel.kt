package tmg.flashback.weekend.ui.qualifying

import tmg.flashback.formula1.model.DriverEntry
import tmg.flashback.formula1.model.QualifyingResult

sealed class QualifyingModel(
    val id: String,
    val isResult: Boolean
) {

    object NotAvailableYet: QualifyingModel(
        id = "not_available_yet",
        isResult = false
    )

    object NotAvailable: QualifyingModel(
        id = "not_available",
        isResult = false
    )

    object Loading: QualifyingModel(
        id = "loading",
        isResult = false
    )

    data class Q1Q2Q3(
        val driver: DriverEntry,
        private val finalQualifyingPosition: Int?,
        val q1: QualifyingResult?,
        val q2: QualifyingResult?,
        val q3: QualifyingResult?,
        val qualified: Int? = finalQualifyingPosition ?: q3?.position ?: q2?.position ?: q1?.position,
        val grid: Int?
    ) : QualifyingModel(driver.driver.id, true) {
        companion object
    }

    data class Q1Q2(
        val driver: DriverEntry,
        private val finalQualifyingPosition: Int?,
        val q1: QualifyingResult?,
        val q2: QualifyingResult?,
        val qualified: Int? = finalQualifyingPosition ?: q2?.position ?: q1?.position
    ) : QualifyingModel(driver.driver.id, true) {
        companion object
    }

    data class Q1(
        val driver: DriverEntry,
        private val finalQualifyingPosition: Int?,
        val q1: QualifyingResult?,
        val qualified: Int? = finalQualifyingPosition ?: q1?.position
    ) : QualifyingModel(driver.driver.id, true) {
        companion object
    }
}