package tmg.f1stats.extensions

fun Int.toEmptyIfZero(): String = if (this == 0) "" else this.toString()