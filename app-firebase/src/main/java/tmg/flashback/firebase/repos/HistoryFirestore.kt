package tmg.flashback.firebase.repos

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tmg.flashback.firebase.FirebaseRepo
import tmg.flashback.firebase.converters.convert
import tmg.flashback.firebase.models.FHistorySeason
import tmg.flashback.repo.db.CrashManager
import tmg.flashback.repo.db.stats.HistoryRepository
import tmg.flashback.repo.models.stats.History
import tmg.flashback.repo.models.stats.WinnerSeason

@ExperimentalCoroutinesApi
class HistoryFirestore(
    crashManager: CrashManager
): FirebaseRepo(crashManager), HistoryRepository {

    override fun allHistory(): Flow<List<History>> {
        return getHistory()
    }

    override fun historyFor(season: Int): Flow<History?> {
        val seasonKey = "${season.toString().substring(0, 3)}0"
        return document("overview/season$seasonKey")
                .getDoc<FHistorySeason>()
                .map {
                    it?.convert()?.firstOrNull { it.season == season }
                }
    }

    override fun allWinners(): Flow<List<WinnerSeason>> {
        return getWinner()
    }

    override fun winnersFor(season: Int): Flow<WinnerSeason?> {
        return getWinner()
            .map { list -> list.firstOrNull { it.season == season } }
    }

    private fun getHistory(): Flow<List<History>> {
        return collection("overview")
            .getDocuments<FHistorySeason>()
            .map { list ->
                list.map { it.convert() }
                    .flatten()
                    .sortedByDescending { it.season }
            }
    }

    private fun getWinner(): Flow<List<WinnerSeason>> {
        return getHistory()
            .map { list -> list
                .map { it.winner }
                .filterNotNull()
            }
    }
}