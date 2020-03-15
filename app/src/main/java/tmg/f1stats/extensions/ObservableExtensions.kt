package tmg.f1stats.extensions

import io.reactivex.rxjava3.core.Observable

fun <T,E> Observable<List<T>>.mapList(callback: (item: T) -> E): Observable<List<E>> {
    return this.map { list ->
        list.map(callback)
    }
}