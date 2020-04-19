package tmg.flashback.repo.utils

fun Int?.toMaxIfZero(): Int = if (this == null || this == 0) Int.MAX_VALUE else this