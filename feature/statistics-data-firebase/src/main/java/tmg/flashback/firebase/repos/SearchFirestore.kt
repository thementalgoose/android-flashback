package tmg.flashback.firebase.repos

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import tmg.crash_reporting.controllers.CrashController
import tmg.flashback.data.db.stats.SearchRepository
import tmg.flashback.data.models.stats.History
import tmg.flashback.data.models.stats.SearchCircuit
import tmg.flashback.data.models.stats.SearchConstructor
import tmg.flashback.data.models.stats.SearchDriver
import tmg.flashback.data.models.stats.SearchRace
import tmg.flashback.firebase.FirebaseRepo
import tmg.flashback.firebase.mappers.HistoryMapper
import tmg.flashback.firebase.mappers.SearchMapper
import tmg.flashback.firebase.models.FHistorySeason
import tmg.flashback.firebase.models.FSearchCircuit
import tmg.flashback.firebase.models.FSearchConstructor
import tmg.flashback.firebase.models.FSearchDriver
import tmg.flashback.firebase.overviewKeys

class SearchFirestore(
    crashController: CrashController,
    private val searchMapper: SearchMapper,
    private val historyMapper: HistoryMapper
): FirebaseRepo(crashController), SearchRepository {

    override fun allDrivers(): Flow<List<SearchDriver>> {
        crashController.log("document(search/drivers)")
        return document("search/drivers")
            .getDoc<FSearchDriver, List<SearchDriver>> {
                (it.drivers ?: emptyMap())
                    .mapNotNull { (driverId, model) ->
                        if (model == null) return@mapNotNull null
                        return@mapNotNull searchMapper.mapSearchDriver(model, driverId)
                    }
            }
            .filterNotNull()
    }

    override fun allConstructors(): Flow<List<SearchConstructor>> {
        crashController.log("document(search/constructors)")
        return document("search/constructors")
            .getDoc<FSearchConstructor, List<SearchConstructor>> {
                (it.constructors ?: emptyMap())
                    .mapNotNull { (constructorId, model) ->
                        if (model == null) return@mapNotNull null
                        return@mapNotNull searchMapper.mapSearchConstructor(model, constructorId)
                    }
            }
            .filterNotNull()
    }

    override fun allCircuits(): Flow<List<SearchCircuit>> {
        crashController.log("document(search/circuits)")
        return document("search/circuits")
            .getDoc<FSearchCircuit, List<SearchCircuit>> {
                (it.circuits ?: emptyMap())
                    .mapNotNull { (circuitId, model) ->
                        if (model == null) return@mapNotNull null
                        return@mapNotNull searchMapper.mapSearchCircuit(model, circuitId)
                    }
            }
            .filterNotNull()
    }

    override fun allRaces(): Flow<List<History>> {
        val historyFlows = overviewKeys.map {
            document("overview/$it")
                .getDoc<FHistorySeason,List<History>> {
                    historyMapper.mapHistory(it)
                }
                .filterNotNull()
        }
        return combine(historyFlows) {
            it.toList().flatten()
        }
    }
}