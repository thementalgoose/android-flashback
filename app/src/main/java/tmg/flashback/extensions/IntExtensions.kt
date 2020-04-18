package tmg.flashback.extensions

fun Int.toEmptyIfZero(): String = if (this == 0) "" else this.toString()