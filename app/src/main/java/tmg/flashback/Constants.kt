package tmg.flashback

import org.threeten.bp.Year
import org.threeten.bp.format.DateTimeFormatter

const val minimumSupportedYear = 1950

// TODO: Figure out a good way to do this
const val currentYear = 2019 // Year.now().value
val supportedYears: List<Int> = List(currentYear - minimumSupportedYear + 1) { minimumSupportedYear + it }

val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

val colours: List<Pair<String, String>> = listOf(
    "#ffcdd2" to "#ef9a9a",
    "#f8bbd0" to "#f48fb1",
    "#e1bee7" to "#ce93d8",
    "#d1c4e9" to "#b39ddb",
    "#c5cae9" to "#9fa8da",
    "#bbdefb" to "#90caf9",
    "#b3e5fc" to "#81d4fa",
    "#b2ebf2" to "#80deea",
    "#b2dfdb" to "#80cbc4",
    "#c8e6c9" to "#a5d6a7",
    "#dcedc8" to "#c5e1a5",
    "#f0f4c3" to "#e6ee9c",
    "#ffe0b2" to "#ffcc80",
    "#ffccbc" to "#ffab91",
    "#cfd8dc" to "#b0bec5"
)