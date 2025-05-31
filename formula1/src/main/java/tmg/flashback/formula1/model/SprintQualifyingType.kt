package tmg.flashback.formula1.model

import tmg.flashback.strings.R.string

enum class SprintQualifyingType {
    SQ1,
    SQ2,
    SQ3
}

val SprintQualifyingType.headerLabel: Int
    get() = when (this) {
        SprintQualifyingType.SQ1 -> string.sprint_qualifying_header_q1
        SprintQualifyingType.SQ2 -> string.sprint_qualifying_header_q2
        SprintQualifyingType.SQ3 -> string.sprint_qualifying_header_q3
    }