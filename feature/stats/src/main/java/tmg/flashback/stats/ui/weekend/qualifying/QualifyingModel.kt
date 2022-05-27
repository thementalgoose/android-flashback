package tmg.flashback.stats.ui.weekend.qualifying

import tmg.flashback.formula1.model.DriverConstructor
import tmg.flashback.formula1.model.RaceQualifyingResult

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
        val driver: DriverConstructor,
        private val finalQualifyingPosition: Int?,
        val q1: RaceQualifyingResult?,
        val q2: RaceQualifyingResult?,
        val q3: RaceQualifyingResult?,
        val qualified: Int? = finalQualifyingPosition ?: q3?.position ?: q2?.position ?: q1?.position
    ) : QualifyingModel(driver.driver.id, true)

    data class Q1Q2(
        val driver: DriverConstructor,
        private val finalQualifyingPosition: Int?,
        val q1: RaceQualifyingResult?,
        val q2: RaceQualifyingResult?,
        val qualified: Int? = finalQualifyingPosition ?: q2?.position ?: q1?.position
    ) : QualifyingModel(driver.driver.id, true)

    data class Q1(
        val driver: DriverConstructor,
        private val finalQualifyingPosition: Int?,
        val q1: RaceQualifyingResult?,
        val qualified: Int? = finalQualifyingPosition ?: q1?.position
    ) : QualifyingModel(driver.driver.id, true)
}