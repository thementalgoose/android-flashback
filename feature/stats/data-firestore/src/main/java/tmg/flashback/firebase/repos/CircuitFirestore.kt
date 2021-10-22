package tmg.flashback.firebase.repos

import kotlinx.coroutines.flow.Flow
import tmg.crash_reporting.controllers.CrashController
import tmg.flashback.firebase.FirebaseRepo
import tmg.flashback.firebase.models.FCircuit
import tmg.flashback.data.db.stats.CircuitRepository
import tmg.flashback.data.models.stats.Circuit
import tmg.flashback.firebase.mappers.CircuitMapper

class CircuitFirestore(
        crashController: CrashController,
        private val circuitMapper: CircuitMapper
) : FirebaseRepo(crashController), CircuitRepository {

    override fun getCircuit(id: String): Flow<Circuit?> {
        crashController.log("document(circuits/$id)")
        return document("circuits/$id")
            .getDoc<FCircuit, Circuit> {
                circuitMapper.mapCircuit(it)
        }
    }
}
