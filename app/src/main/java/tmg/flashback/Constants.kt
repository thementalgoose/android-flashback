package tmg.flashback

import org.threeten.bp.Year

const val minimumSupportedYear = 2000

// TODO: Figure out a good way to do this
val currentYear = 2019 // Year.now().value
val supportedYears: List<Int> = List(currentYear - minimumSupportedYear + 1) { minimumSupportedYear + it }