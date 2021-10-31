package tmg.flashback.firebase.repos

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import tmg.crash_reporting.controllers.CrashController
import tmg.flashback.data.db.stats.SearchRepository
import tmg.flashback.formula1.model.History
import tmg.flashback.formula1.model.SearchCircuit
import tmg.flashback.formula1.model.SearchConstructor
import tmg.flashback.formula1.model.SearchDriver
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

    override fun allDrivers(): Flow<List<tmg.flashback.formula1.model.SearchDriver>> {
        crashController.log("document(search/drivers)")
        return document("search/drivers")
            .getDoc<FSearchDriver, List<tmg.flashback.formula1.model.SearchDriver>> {
                (it.drivers ?: emptyMap())
                    .mapNotNull { (driverId, model) ->
                        if (model == null) return@mapNotNull null
                        return@mapNotNull searchMapper.mapSearchDriver(model, driverId)
                    }
            }
            .filterNotNull()
    }

    override fun allConstructors(): Flow<List<tmg.flashback.formula1.model.SearchConstructor>> {
        crashController.log("document(search/constructors)")
        return document("search/constructors")
            .getDoc<FSearchConstructor, List<tmg.flashback.formula1.model.SearchConstructor>> {
                (it.constructors ?: emptyMap())
                    .mapNotNull { (constructorId, model) ->
                        if (model == null) return@mapNotNull null
                        return@mapNotNull searchMapper.mapSearchConstructor(model, constructorId)
                    }
            }
            .filterNotNull()
    }

    override fun allCircuits(): Flow<List<tmg.flashback.formula1.model.SearchCircuit>> {
        crashController.log("document(search/circuits)")
        return document("search/circuits")
            .getDoc<FSearchCircuit, List<tmg.flashback.formula1.model.SearchCircuit>> {
                (it.circuits ?: emptyMap())
                    .mapNotNull { (circuitId, model) ->
                        if (model == null) return@mapNotNull null
                        return@mapNotNull searchMapper.mapSearchCircuit(model, circuitId)
                    }
            }
            .filterNotNull()
    }

    override fun allRaces(): Flow<List<tmg.flashback.formula1.model.History>> {
        val historyFlows = overviewKeys.map {
            document("overview/$it")
                .getDoc<FHistorySeason,List<tmg.flashback.formula1.model.History>> {
                    historyMapper.mapHistory(it)
                }
                .filterNotNull()
        }
        return combine(historyFlows) {
            it.toList().flatten()
        }
    }
}