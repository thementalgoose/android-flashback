package tmg.f1stats

import org.threeten.bp.Year

const val minimumSupportedYear = 1950
val currentYear = Year.now().value
val supportedYears: List<Int> = List(currentYear - minimumSupportedYear + 1) { minimumSupportedYear + it }