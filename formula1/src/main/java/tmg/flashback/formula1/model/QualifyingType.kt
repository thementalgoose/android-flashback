package tmg.flashback.formula1.model

import tmg.flashback.strings.R.string

enum class QualifyingType(
    val order: Int
) {
    Q1(3),
    Q2(2),
    Q3(1)
}

val QualifyingType.headerLabel: Int
    get() = when (this) {
        QualifyingType.Q1 -> string.qualifying_header_q1
        QualifyingType.Q2 -> string.qualifying_header_q2
        QualifyingType.Q3 -> string.qualifying_header_q3
    }