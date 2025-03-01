package tmg.flashback.providers.previewdata

import tmg.flashback.formula1.model.QualifyingResult
import tmg.flashback.formula1.model.QualifyingRound
import tmg.flashback.formula1.model.QualifyingType

internal fun QualifyingRound.Companion.model(
    label: QualifyingType = QualifyingType.Q1,
    order: Int = 1,
    results: List<QualifyingResult> = listOf(
        QualifyingResult.model()
    )
): QualifyingRound = QualifyingRound(
    label = label,
    order = order,
    results = results
)