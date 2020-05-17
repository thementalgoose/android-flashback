package tmg.flashback.extensions

fun Int.toEmptyIfZero(): String = if (this == 0) "" else this.toString()

/**
 * Convert a number into it's "ordinal abbreviation" or "elevated terminal"
 * Basically converting 1 into 1st, 11 into 11th, 33 into 33rd
 */
val Int.elevatedTerminal: String
    get() = this.ordinalAbbreviation

/**
 * Convert a number into it's "ordinal abbreviation" or "elevated terminal"
 * Basically converting 1 into 1st, 11 into 11th, 33 into 33rd
 */
val Int.ordinalAbbreviation: String
    get() {
        val string = this.toString()
        return if (this <= 0) {
            string
        } else if (string.endsWith("11") ||
            string.endsWith("12") ||
            string.endsWith("13")) {
            "${this}th"
        } else if (string.endsWith("1")) {
            "${this}st"
        } else if (string.endsWith("2")) {
            "${this}nd"
        } else if (string.endsWith("3")) {
            "${this}rd"
        } else {
            "${this}th"
        }
    }