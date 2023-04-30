package tmg.flashback.domain.repo.extensions

fun <T,R> Map<T, R>?.valueList(): List<R> {
    return (this?.values ?: emptyList()).toMutableList()
}