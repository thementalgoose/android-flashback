package tmg.flashback.repo_firebase.repos

import tmg.flashback.repo.db.CrashReporter
import tmg.flashback.repo.db.HistoryDB
import tmg.flashback.repo.models.History
import tmg.flashback.repo_firebase.converters.convert
import tmg.flashback.repo_firebase.firebase.getDocument
import tmg.flashback.repo_firebase.models.FHistorySeason
import tmg.flashback.repo_firebase.version

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
        return getDocument(FHistorySeason::class.java, "version/$version/history/season") { model, _ -> model.convert() } ?: emptyList()
    }
}