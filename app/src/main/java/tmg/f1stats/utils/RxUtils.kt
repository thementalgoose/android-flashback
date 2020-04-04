package tmg.f1stats.utils

import io.reactivex.rxjava3.core.Observable

object RxUtils {
    fun <T> ongoing(value: T): Observable<T> {
        return Observable.create<T> { emitter ->
            emitter.onNext(value)
        }
    }
}