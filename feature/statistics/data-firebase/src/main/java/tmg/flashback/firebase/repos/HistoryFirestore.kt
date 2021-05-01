package tmg.flashback.firebase.repos

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tmg.crash_reporting.controllers.CrashController
import tmg.flashback.firebase.FirebaseRepo
import tmg.flashback.firebase.converters.convert
import tmg.flashback.firebase.models.FHistorySeason
import tmg.flashback.data.db.stats.HistoryRepository
import tmg.flashback.data.models.stats.History

class HistoryFirestore(
    crashController: CrashController
): FirebaseRepo(crashController), HistoryRepository {

    override fun historyFor(season: Int): Flow<History?> {
        val seasonKey = "${season.toString().substring(0, 3)}0"
        crashController.log("document(overview/$seasonKey) $season")
        return document("overview/season$seasonKey")
                .getDoc<FHistorySeason,List<History>> { it.convert() }
                .map { list ->
                    list ?.firstOrNull { it.season == season }
                }
    }
}