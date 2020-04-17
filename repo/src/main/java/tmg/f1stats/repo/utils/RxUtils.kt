package tmg.f1stats.repo.utils

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import tmg.f1stats.repo.Optional

val backgroundScheduler: Scheduler = Schedulers.io()

fun <T, E> Observable<Optional<T>>.mapOptionalValue(mapMethod: (model: T) -> E): Observable<Optional<E>> {
    return this.map {
        if (it.isNull) {
            Optional()
        }
        else {
            Optional(mapMethod(it.value!!))
        }
    }
}

fun <T, E> Observable<T>.mapToOptional(mapMethod: (model: T) -> E?): Observable<Optional<E>> {
    return this.map {
        Optional(mapMethod(it))
    }
}

fun <T> Observable<Optional<T>>.filterNotNull(): Observable<T> {
    return this
        .filter { !it.isNull }
        .map { it.value!! }
}