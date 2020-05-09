package tmg.flashback

import org.threeten.bp.Year
import org.threeten.bp.format.DateTimeFormatter

const val minimumSupportedYear = 1950

// TODO: Figure out a good way to do this
const val currentYear = 2019 // Year.now().value
val supportedYears: List<Int> = List(currentYear - minimumSupportedYear + 1) { minimumSupportedYear + it }

val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")