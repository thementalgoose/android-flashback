package tmg.flashback.firebase.repos

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tmg.flashback.firebase.FirebaseRepo
import tmg.flashback.firebase.converters.convert
import tmg.flashback.firebase.crash.FirebaseCrashManager
import tmg.flashback.firebase.models.FHistorySeason
import tmg.flashback.repo.db.stats.HistoryRepository
import tmg.flashback.repo.models.stats.History
import tmg.flashback.repo.models.stats.WinnerSeason

class HistoryFirestore(
    crashManager: FirebaseCrashManager
): FirebaseRepo(crashManager), HistoryRepository {

    override fun historyFor(season: Int): Flow<History?> {
        crashManager.logInfo("HistoryFirestore.historyFor($season)")
        val seasonKey = "${season.toString().substring(0, 3)}0"
        return document("overview/season$seasonKey")
                .getDoc<FHistorySeason,List<History>> { it.convert() }
                .map { list ->
                    list ?.firstOrNull { it.season == season }
                }
    }
}