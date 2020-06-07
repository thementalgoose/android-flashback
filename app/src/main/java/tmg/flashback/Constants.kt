package tmg.flashback

import org.threeten.bp.Year
import org.threeten.bp.format.DateTimeFormatter

const val minimumSupportedYear = 1950
val currentYear: Int
    get() = Year.now().value
val allYears: List<Int>
    get() = (minimumSupportedYear..currentYear).map { it }

const val bottomSheetFastScrollDuration = 300

val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

val coloursDecade: Map<String, String> = mapOf(
    "1950" to "#9fa8da",
    "1960" to "#ce93d8",
    "1970" to "#ef9a9a",
    "1980" to "#90caf9",
    "1990" to "#a5d6a7",
    "2000" to "#81d4fa",
    "2010" to "#b0bec5",
    "2020" to "#f48fb1",
    "2030" to "#b39ddb",
    "2040" to "#c5e1a5",
    "2050" to "#80cbc4",
    "2060" to "#b0bec5",
    "2070" to "#ffcc80",
    "2080" to "#ffab91",
    "2090" to "#80deea"
)

val colours: List<Pair<String, String>> = listOf( // Dark to light
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