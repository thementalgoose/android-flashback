package tmg.flashback.repo_firebase.repos

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import tmg.flashback.repo.db.CrashReporter
import tmg.flashback.repo.db.HistoryDB
import tmg.flashback.repo.models.History
import tmg.flashback.repo.models.WinnerSeason
import tmg.flashback.repo_firebase.converters.convert
import tmg.flashback.repo_firebase.firebase.FirebaseRepo
import tmg.flashback.repo_firebase.firebase.getDocument
import tmg.flashback.repo_firebase.models.FHistorySeason
import tmg.flashback.repo_firebase.models.FWinner
import tmg.flashback.repo_firebase.version

class HistoryFirestore(
    crashReporter: CrashReporter
): FirebaseRepo(crashReporter), HistoryDB {

    override fun allHistory(): Flow<List<History>> {
        return getHistory()
    }

    override fun historyFor(season: Int): Flow<History?> {
        return getHistory()
            .map { list -> list.firstOrNull { it.season == season } }
    }

    override fun allWinners(): Flow<List<WinnerSeason>> {
        return getWinner()
    }

    override fun winnersFor(season: Int): Flow<WinnerSeason?> {
        return getWinner()
            .map { list -> list.firstOrNull { it.season == season } }
    }

    private fun getHistory(): Flow<List<History>> {
        return document("version/$version/history/season")
            .getDoc<FHistorySeason>()
            .convertModel { it?.convert() ?: emptyList() }
    }

    private fun getWinner(): Flow<List<WinnerSeason>> {
        return document("version/$version/history/winner")
            .getDoc<FWinner>()
            .convertModel { it?.convert() ?: emptyList() }
    }
}