package tmg.f1stats.repo.db

import io.reactivex.rxjava3.core.Observable
import tmg.f1stats.repo.Optional
import tmg.f1stats.repo.models.History

interface HistoryDB {
    fun allHistory(): Observable<List<History>>
    fun historyFor(season: Int): Observable<Optional<History>>
}