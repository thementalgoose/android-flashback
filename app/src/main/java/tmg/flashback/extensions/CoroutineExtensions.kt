package tmg.flashback.extensions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import tmg.flashback.utils.DataEvent

fun <T,E> Flow<T>.combinePair(second: Flow<E>): Flow<Pair<T, E>> {
    return this.combine(second) { a, b -> Pair(a, b) }
}

fun <T,E,R> Flow<T>.combineTriple(second: Flow<E>, third: Flow<R>): Flow<Triple<T, E, R>> {
    return this
        .combine(second) { a, b -> Pair(a, b) }
        .combine(third) { firstSecond, thirdVal -> Triple(firstSecond.first, firstSecond.second, thirdVal) }
}

fun <T> Flow<T>.then(action: (data: T) -> Unit): Flow<T> {
    return this.map {
        action(it)
        it
    }
}

fun <T> Flow<T>.toDataEvent(): Flow<DataEvent<T>> {
    return this.map { DataEvent(it) }
}

fun <T> Flow<T?>.filterNotNull(): Flow<T> {
    return this
        .filter { it != null }
        .map { it!! }
}

fun <T> Flow<List<T>>.filterNotEmpty(): Flow<List<T>> {
    return this
        .filter { it.isNotEmpty() }
}

fun <T> Flow<T?>.filterNull(): Flow<Unit> {
    return this
        .filter { it == null }
        .map { Unit }
}