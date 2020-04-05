package tmg.f1stats.repo_firebase.repos

import io.reactivex.rxjava3.core.Observable
import tmg.f1stats.repo.Optional
import tmg.f1stats.repo.db.HistoryDB
import tmg.f1stats.repo.models.History
import tmg.f1stats.repo.utils.filterNotNull
import tmg.f1stats.repo.utils.firstOrOptional
import tmg.f1stats.repo_firebase.converters.convert
import tmg.f1stats.repo_firebase.firebase.getDocument
import tmg.f1stats.repo_firebase.models.FHistorySeason
import tmg.f1stats.repo_firebase.models.FHistorySeasonRound
import tmg.f1stats.repo_firebase.models.FSeason

class HistoryFirestore: HistoryDB {

    override fun allHistory(): Observable<List<History>> {
        return getHistory()
    }

    override fun historyFor(season: Int): Observable<Optional<History>> {
        return getHistory()
            .map { history -> history.firstOrOptional { it.season == season } }
    }

    private fun getHistory(): Observable<List<History>> {
        return getDocument(FHistorySeason::class.java, "history/season") { model, _ -> model.convert() }
            .filterNotNull()
    }
}