package tmg.flashback.firebase.repos

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tmg.flashback.repo.db.CrashReporter
import tmg.flashback.repo.db.stats.HistoryDB
import tmg.flashback.repo.models.stats.History
import tmg.flashback.repo.models.stats.WinnerSeason
import tmg.flashback.firebase.converters.convert
import tmg.flashback.firebase.FirebaseRepo
import tmg.flashback.firebase.models.FHistorySeason
import tmg.flashback.firebase.models.FWinner
import tmg.flashback.firebase.version

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