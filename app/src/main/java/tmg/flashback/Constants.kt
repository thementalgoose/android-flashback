package tmg.flashback

import org.threeten.bp.Year

const val minimumSupportedYear = 2000
val currentYear = Year.now().value
val supportedYears: List<Int> = List(currentYear - minimumSupportedYear + 1) { minimumSupportedYear + it }