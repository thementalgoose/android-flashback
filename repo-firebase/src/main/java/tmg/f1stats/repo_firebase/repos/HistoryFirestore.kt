package tmg.f1stats.repo_firebase.repos

import tmg.f1stats.repo.db.CrashReporter
import tmg.f1stats.repo.db.HistoryDB
import tmg.f1stats.repo.models.History
import tmg.f1stats.repo_firebase.converters.convert
import tmg.f1stats.repo_firebase.firebase.getDocument
import tmg.f1stats.repo_firebase.models.FHistorySeason

class HistoryFirestore(
    private val crashReporter: CrashReporter
): HistoryDB {

    override suspend fun allHistory(): List<History> {
        return getHistory()
    }

    override suspend fun historyFor(season: Int): History? {
        return getHistory()
            .firstOrNull { history -> history.season == season }
    }

    private suspend fun getHistory(): List<History> {
        return getDocument(FHistorySeason::class.java, "history/season") { model, _ -> model.convert() } ?: emptyList()
    }
}