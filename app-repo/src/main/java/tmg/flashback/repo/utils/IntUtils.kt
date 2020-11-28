package tmg.flashback.repo.utils

fun Int?.toMaxIfZero(): Int = if (this == null || this == 0) Int.MAX_VALUE else this

fun Int.extendTo(toCharacters: Int = 2): String {
    var chars: String = ""
    for (i in this.toString().length until toCharacters) {
        chars += "0"
    }
    return "$chars$this"
}

fun Int.positive(): Int = when {
    this < 0 -> 0
    else -> this
}