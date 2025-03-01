package tmg.flashback.providers.previewdata

import tmg.flashback.formula1.model.SprintQualifyingResult
import tmg.flashback.formula1.model.SprintQualifyingRound
import tmg.flashback.formula1.model.SprintQualifyingType

internal fun SprintQualifyingRound.Companion.model(
    label: SprintQualifyingType = SprintQualifyingType.SQ1,
    order: Int = 1,
    results: List<SprintQualifyingResult> = listOf(
        SprintQualifyingResult.model()
    )
): SprintQualifyingRound = SprintQualifyingRound(
    label = label,
    order = order,
    results = results
)